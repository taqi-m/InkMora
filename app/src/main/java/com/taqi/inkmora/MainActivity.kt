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
import androidx.navigation.toRoute
import com.taqi.inkmora.ui.screens.NoteEditorScreen
import com.taqi.inkmora.ui.screens.NoteListScreen
import com.taqi.inkmora.ui.screens.Screen
import com.taqi.inkmora.ui.screens.SplashScreen
import com.taqi.inkmora.ui.screens.ThemePromptSheet
import com.taqi.inkmora.ui.theme.InkMoraTheme
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
            InkMoraTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash,
                ) {
                    composable<Screen.Splash> {
                        SplashScreen {
                            navController.navigate(Screen.NoteList) {
                                popUpTo(Screen.Splash) { inclusive = true }
                            }
                        }
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
                            onRetry = { viewModel.retry() }
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