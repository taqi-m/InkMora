package com.taqi.inkmora.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taqi.inkmora.R
import com.taqi.inkmora.domain.model.Note
import com.taqi.inkmora.ui.theme.*

/**
 * UI State for the Note List Screen
 */
sealed interface NoteListUiState {
    object Loading : NoteListUiState
    object Empty : NoteListUiState
    data class Error(val message: String) : NoteListUiState
    data class Success(val notes: List<Note>) : NoteListUiState
}

@Composable
fun NoteListScreen(
    state: NoteListUiState,
    onCreateNote: () -> Unit,
    onEditNote: (Int) -> Unit,
    onDeleteNote: (Note) -> Unit = {},
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyStaggeredGridState()
    val isFabExpanded by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    Scaffold(
        topBar = { NoteListTopBar() },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateNote,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                expanded = isFabExpanded,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_nib),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                text = {
                    Text(
                        text = "Uncap Ink",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state) {
                is NoteListUiState.Loading -> LoadingState()
                is NoteListUiState.Empty -> EmptyState()
                is NoteListUiState.Error -> ErrorState(state.message, onRetry)
                is NoteListUiState.Success -> {
                    NoteListContent(
                        notes = state.notes,
                        onNoteClick = { onEditNote(it.id ?: 0) },
                        onNoteDelete = onDeleteNote,
                        listState = listState
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteListTopBar() {
// ...
// (Omitted unchanged TopBar)
    CenterAlignedTopAppBar(
        title = {
            Surface(
                onClick = { /* Open Search */ },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Find my urgent notes...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* Open Menu */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Open Profile */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_account_circle),
                    contentDescription = "Profile"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteListContent(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onNoteDelete: (Note) -> Unit = {},
    listState: LazyStaggeredGridState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "My Notes",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                )
                Text(
                    text = "${notes.size} thoughts captured",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(250.dp),
            state = listState,
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(notes, key = { it.id ?: it.hashCode() }) { note ->
                NoteCard(
                    note = note,
                    onClick = { onNoteClick(note) }
                )
            }
        }
    }
}


@Composable
private fun NoteCard(
    note: Note,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                
                // Show pin if prioritized (mock logic)
                if (note.title.contains("urgent", ignoreCase = true)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pin),
                        contentDescription = "Pinned",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Oct 12, 2023", // Placeholder date
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                
                // AI Sparkle Indicator (Mock)
                Icon(
                    painter = painterResource(id = R.drawable.ic_ai_sparkles),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = AuraPurple
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    val infiniteTransition = rememberInfiniteTransition()
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Outer Ink Bloom Glow
        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(scale * 1.5f)
                .alpha(0.15f)
                .background(AuraPurple, CircleShape)
        )
        
        // Inner Pulsing Nib
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier
                .size(80.dp)
                .scale(scale)
                .alpha(alpha)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_draw),
                    contentDescription = "Loading...",
                    tint = AuraPurple,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .alpha(0.3f),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Start writing. InkMora will find the mood.",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Your thoughts belong here. The canvas is clear, waiting for your first ink.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.alpha(0.8f)
        )
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_error_smudge),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "A smudge in the ink.",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedButton(
            onClick = onRetry,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Wipe and Retry")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteListScreenPreview() {
    val dummyNotes = listOf(
        Note(
            title = "Morning Reflections on Silence",
            content = "The quiet before the city wakes is the most honest time of day...",
            timestamp = System.currentTimeMillis(),
            color = 0xFFC6C4DF.toInt(),
            id = 1
        ),
        Note(
            title = "Project Beta Launch Ideas",
            content = "Need to hit the ground running. Focus on the core user flow first...",
            timestamp = System.currentTimeMillis(),
            color = 0xFFB9AA83.toInt(),
            id = 2
        ),
        Note(
            title = "Grocery List",
            content = "Almond milk, avocados, whole grain bread, dark chocolate...",
            timestamp = System.currentTimeMillis(),
            color = 0xFFFFFFFF.toInt(),
            id = 3
        )
    )

    InkMoraTheme {
        NoteListScreen(
            state = NoteListUiState.Success(dummyNotes),
            onCreateNote = {},
            onEditNote = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoteListScreenEmptyPreview() {
    InkMoraTheme {
        NoteListScreen(
            state = NoteListUiState.Empty,
            onCreateNote = {},
            onEditNote = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoteListScreenLoadingPreview() {
    InkMoraTheme {
        NoteListScreen(
            state = NoteListUiState.Loading,
            onCreateNote = {},
            onEditNote = {},
            onRetry = {}
        )
    }
}
