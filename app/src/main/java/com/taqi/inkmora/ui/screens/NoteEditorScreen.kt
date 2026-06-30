package com.taqi.inkmora.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taqi.inkmora.R
import com.taqi.inkmora.domain.model.AppMood
import com.taqi.inkmora.ui.theme.AuraPurple
import com.taqi.inkmora.ui.theme.InkMoraTheme
import com.taqi.inkmora.ui.theme.InterFontFamily
import com.taqi.inkmora.ui.viewmodels.NoteEditorUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    state: NoteEditorUiState,
    currentMood: AppMood,
    onMoodSelect: (AppMood) -> Unit,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSaveNote: () -> Unit,
    onDeleteNote: () -> Unit,
    onBack: () -> Unit
) {
    // Simulate AI Active state for the visual glow
    val isAiActive = false

    val scrollState = rememberScrollState()

    // Focus requester variables to handle tapping empty space
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    val sheetState = rememberModalBottomSheetState()
    var showThemeSheet by remember { mutableStateOf(false) }

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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                // Make the entire Box clickable to focus the text field
                .clickable(
                    interactionSource = interactionSource,
                    indication = null // Removes the ripple effect
                ) {
                    focusRequester.requestFocus()
                }
        ) {
            // Optional: Background Mood Gradient Wash would go here
            if (isAiActive) {
                // Background wash implementation placeholder
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding() // Ensures scroll bounds adjust when keyboard opens
                    .verticalScroll(scrollState) // Re-enabled vertical scrolling
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            ) {
                // Title Field
                val titleStyle = MaterialTheme.typography.headlineLarge
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
                val metadataColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
                val metadataStyle = MaterialTheme.typography.labelSmall.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.Normal, letterSpacing = 0.5.sp)

                Row(
                    modifier = Modifier.padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dateString,
                        style = metadataStyle,
                        color = metadataColor
                    )

                    if (state.content.isNotEmpty()) {
                        val wordCount = remember(state.content) { state.content.split(Regex("\\s+")).filter { it.isNotBlank() }.size }
                        val readTime = remember(wordCount) { (wordCount / 200).coerceAtLeast(1) }

                        Text(
                            text = " · $wordCount words · ~$readTime min read",
                            style = metadataStyle,
                            color = metadataColor
                        )
                    }
                }

                val contentTextStyle = MaterialTheme.typography.bodyLarge
                // Content Field
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester), // Attached the focus requester here
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
                    }
                )

                // Overscroll clearance spacer to push text above the AI mood pill
                Spacer(modifier = Modifier.height(80.dp))
            }

            // The Signature Element: AI Mood Pill (F4 - Keyboard Aware)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .imePadding()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AiMoodPill(
                    onClick = { showThemeSheet = true }
                )
            }

            if (showThemeSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showThemeSheet = false },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.surface,
                    dragHandle = { BottomSheetDefaults.DragHandle() }
                ) {
                    ThemePromptSheet(
                        currentMood = currentMood,
                        onMoodSelect = onMoodSelect,
                        onApply = { showThemeSheet = false }
                    )
                }
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

@Preview(showBackground = true,
    device = "spec:width=1080px,height=2340px,dpi=440,navigation=buttons"
)
@Composable
fun NoteEditorScreenPreview() {
    InkMoraTheme {
        NoteEditorScreen(
            state = NoteEditorUiState(
                title = "Reflections on the Ink",
                content = "The way the digital nib moves across the screen is reminiscent of traditional calligraphy, yet it carries a weightless potential that only the virtual world can offer.",
                timestamp = System.currentTimeMillis()
            ),
            currentMood = AppMood.DEFAULT,
            onMoodSelect = {},
            onTitleChange = {},
            onContentChange = {},
            onSaveNote = {},
            onDeleteNote = {},
            onBack = {}
        )
    }
}
