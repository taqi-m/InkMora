package com.taqi.inkmora.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taqi.inkmora.R
import com.taqi.inkmora.ui.theme.AuraPurple
import com.taqi.inkmora.ui.theme.InkMoraTheme
import com.taqi.inkmora.ui.theme.InterFontFamily
import com.taqi.inkmora.ui.viewmodels.NoteEditorUiState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NoteEditorScreen(
    state: NoteEditorUiState,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSaveNote: () -> Unit,
    onDeleteNote: () -> Unit,
    onBack: () -> Unit,
    onOpenThemePrompt: () -> Unit
) {
    // Simulate AI Active state for the visual glow
    val isAiActive = false

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            NoteEditorTopBar(
                onBack = onBack, 
                onSave = onSaveNote,
                onDelete = onDeleteNote,
                showDelete = state.noteId != null // Only show delete if editing existing note
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0) // Edge-to-edge feel
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding() // Move keyboard awareness to the container
        ) {
            // Optional: Background Mood Gradient Wash would go here
            if (isAiActive) {
                // Background wash implementation placeholder
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState) // Enable vertical scrolling for the whole page
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp)
            ) {
                // Title Field
                val titleStyle = MaterialTheme.typography.displayMedium
                BasicTextField(
                    value = state.title,
                    onValueChange = onTitleChange,
                    textStyle = titleStyle.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        Box {
                            if (state.title.isEmpty()) {
                                Text(
                                    text = "Untitled Note",
                                    style = titleStyle,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Timestamp Metadata Line (F3)
                val dateFormat = remember { SimpleDateFormat("EEEE, d MMM · h:mm a", Locale.getDefault()) }
                val dateString = remember(state.timestamp) { dateFormat.format(Date(state.timestamp)) }
                
                Row(
                    modifier = Modifier.padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dateString,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 0.5.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    
                    if (state.content.isNotEmpty()) {
                        val wordCount = remember(state.content) { state.content.split(Regex("\\s+")).filter { it.isNotBlank() }.size }
                        val readTime = remember(wordCount) { (wordCount / 200).coerceAtLeast(1) }
                        
                        Text(
                            text = " · $wordCount words · ~$readTime min read",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                letterSpacing = 0.5.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    }
                }

                val contentTextStyle = MaterialTheme.typography.bodyLarge
                // Content Field
                BasicTextField(
                    value = state.content,
                    onValueChange = onContentChange,
                    textStyle = contentTextStyle.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (state.content.isEmpty()) {
                                Text(
                                    text = "Start writing...",
                                    style = contentTextStyle,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier.fillMaxWidth() // Don't use fillMaxSize in scrollable Column
                )
                
                // Safety buffer to ensure text can be scrolled above the floating Pill
                Spacer(modifier = Modifier.height(160.dp))
            }

            // The Signature Element: AI Mood Pill (F4 - Keyboard Aware)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp), // Removed imePadding as it's now on the parent Box
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AiMoodPill(
                    onClick = onOpenThemePrompt
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteEditorTopBar(
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    showDelete: Boolean
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Navigate back",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        actions = {
            if (showDelete) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "Delete Note",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            IconButton(
                onClick = { /* Handle AI specific action or open sheet */ },
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_ai_sparkles),
                    contentDescription = "AI Options",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onSave,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "Save Note",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        )
    )
}

@Composable
private fun AiMoodPill(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // Setup infinite transition for the pulsing glow effect
    val infiniteTransition = rememberInfiniteTransition()
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
        shadowElevation = 8.dp, // Replaces tailwind shadow for compose
        modifier = modifier
            .border(
                width = 1.dp,
                color = AuraPurple.copy(alpha = 0.2f),
                shape = CircleShape
            )
            .padding(horizontal = 0.dp) // Reset surface internal padding
    ) {
        // We use a Box to apply the backdrop blur effect behind the content
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "AI Mood",
                    tint = AuraPurple,
                    modifier = Modifier
                        .size(20.dp)
                        .alpha(pulseGlow) // Apply the pulse animation
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Find the mood...",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteEditorScreenPreview() {
    InkMoraTheme {
        NoteEditorScreen(
            state = NoteEditorUiState(
                title = "Reflections on the Ink",
                content = "The way the digital nib moves across the screen is reminiscent of traditional calligraphy, yet it carries a weightless potential that only the virtual world can offer.",
                timestamp = System.currentTimeMillis()
            ),
            onTitleChange = {},
            onContentChange = {},
            onSaveNote = {},
            onDeleteNote = {},
            onBack = {},
            onOpenThemePrompt = {}
        )
    }
}
