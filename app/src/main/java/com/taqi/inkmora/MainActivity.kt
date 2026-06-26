package com.taqi.inkmora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.toRoute
import com.taqi.inkmora.domain.model.AuthUser
import com.taqi.inkmora.ui.screens.*
import com.taqi.inkmora.ui.theme.InkMoraTheme
import com.taqi.inkmora.ui.viewmodels.AuthViewModel
import com.taqi.inkmora.ui.viewmodels.MainViewModel
import com.taqi.inkmora.ui.viewmodels.NoteEditorViewModel
import com.taqi.inkmora.ui.viewmodels.NoteListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val authViewModel: AuthViewModel = hiltViewModel()
            
            val themeSettings by mainViewModel.themeSettings.collectAsStateWithLifecycle()
            val authUser by authViewModel.user.collectAsStateWithLifecycle()

            InkMoraTheme(themeSettings = themeSettings) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash,
                ) {
                    composable<Screen.Splash> {
                        SplashScreen {
                            val nextScreen = if (themeSettings.isOnboardingComplete) Screen.NoteList else Screen.Onboarding
                            navController.navigate(nextScreen) {
                                popUpTo(Screen.Splash) { inclusive = true }
                            }
                        }
                    }
                    composable<Screen.Onboarding> {
                        val authIsSigningIn by authViewModel.isSigningIn.collectAsStateWithLifecycle()
                        
                        OnboardingScreen(
                            currentMood = themeSettings.mood,
                            onMoodSelected = mainViewModel::updateMood,
                            onGoogleSignIn = { context -> authViewModel.signInWithGoogle(context) },
                            isSigningIn = authIsSigningIn,
                            onOnboardingComplete = {
                                mainViewModel.completeOnboarding()
                                navController.navigate(Screen.NoteList) {
                                    popUpTo(Screen.Onboarding) { inclusive = true }
                                }
                                navController.navigate(Screen.NoteEditor())
                            }
                        )
                    }
                    composable<Screen.NoteList> {
                        val viewModel: NoteListViewModel = hiltViewModel()
                        val state by viewModel.uiState.collectAsStateWithLifecycle()

                        NoteListScreen(
                            state = state,
                            onCreateNote = {
                                navController.navigate(Screen.NoteEditor())
                            },
                            onEditNote = { id ->
                                navController.navigate(Screen.NoteEditor(id))
                            },
                            onProfileClick = {
                                navController.navigate(Screen.Profile)
                            },
                            onRetry = { viewModel.retry() }
                        )
                    }
                    composable<Screen.Profile> {
                        ProfileScreen(
                            viewModel = authViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable<Screen.NoteEditor> { backStackEntry ->
                        val route = backStackEntry.toRoute<Screen.NoteEditor>()
                        val viewModel: NoteEditorViewModel = androidx.hilt.navigation.compose.hiltViewModel()
                        val state by viewModel.uiState.collectAsStateWithLifecycle()

                        androidx.compose.runtime.LaunchedEffect(route.noteId) {
                            if (route.noteId != null) {
                                viewModel.loadNote(route.noteId)
                            }
                        }

                        androidx.compose.runtime.LaunchedEffect(key1 = true) {
                            viewModel.eventFlow.collectLatest { event ->
                                when(event) {
                                    is com.taqi.inkmora.ui.viewmodels.NoteEditorEvent.SaveNote -> {
                                        navController.navigateUp()
                                    }
                                    is com.taqi.inkmora.ui.viewmodels.NoteEditorEvent.ShowSnackbar -> {
                                        // Ignore for now, would need a scaffold state to show it
                                    }
                                }
                            }
                        }

                        NoteEditorScreen(
                            state = state,
                            onTitleChange = viewModel::onTitleChange,
                            onContentChange = viewModel::onContentChange,
                            onSaveNote = viewModel::saveNote,
                            onDeleteNote = viewModel::deleteNote,
                            onBack = {
                                navController.popBackStack()
                            },
                            onOpenThemePrompt = {
                                navController.navigate(Screen.ThemePrompt)
                            }
                        )
                    }
                    composable<Screen.ThemePrompt> {
                        ThemePromptSheet(
                            currentMood = themeSettings.mood,
                            onMoodSelect = mainViewModel::updateMood,
                            onApply = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}