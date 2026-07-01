package com.taqi.inkmora.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taqi.inkmora.domain.model.InvalidNoteException
import com.taqi.inkmora.domain.model.Note
import com.taqi.inkmora.domain.model.ThemeToken
import com.taqi.inkmora.domain.usecase.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject

data class NoteEditorUiState(
    val title: String = "",
    val content: String = "",
    val noteId: Int? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isLoading: Boolean = false,
    val color: Int = 0xFFF5F5F7.toInt(), // Default background color
    val themeToken: ThemeToken = ThemeToken.Default,
    val isAnalyzingTheme: Boolean = false
)

sealed class NoteEditorEvent {
    data class ShowSnackbar(val message: String) : NoteEditorEvent()
    object SaveNote : NoteEditorEvent()
}

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditorUiState())
    val uiState: StateFlow<NoteEditorUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<NoteEditorEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null
    private var themeAnalysisJob: Job? = null

    // We can extract arguments directly from the SavedStateHandle if passed via Navigation
    // However, for explicit loading, we can expose a function.

    fun loadNote(noteId: Int) {
        if (noteId != -1 && currentNoteId != noteId) {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true)
                noteUseCases.getNote(noteId)?.also { note ->
                    currentNoteId = note.id
                    _uiState.value = _uiState.value.copy(
                        title = note.title,
                        content = note.content,
                        noteId = note.id,
                        timestamp = note.timestamp,
                        color = note.color,
                        themeToken = if (note.themeSeedColor != null && note.themeStyleName != null) {
                            ThemeToken(note.themeSeedColor, note.themeStyleName, note.themeLabel)
                        } else {
                            ThemeToken.Default
                        },
                        isLoading = false
                    )
                } ?: run {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _eventFlow.emit(NoteEditorEvent.ShowSnackbar("Note not found"))
                }
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.value = _uiState.value.copy(title = newTitle)
    }

    fun onContentChange(newContent: String) {
        _uiState.value = _uiState.value.copy(content = newContent)
    }

    fun analyzeMood() {
        val currentContent = _uiState.value.content
        if (currentContent.length < 10) return
        
        themeAnalysisJob?.cancel()
        themeAnalysisJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAnalyzingTheme = true)
            val result = noteUseCases.analyzeNoteMood(currentContent)
            result.onSuccess { token ->
                _uiState.value = _uiState.value.copy(
                    themeToken = token,
                    color = token.seedColor,
                    isAnalyzingTheme = false
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(isAnalyzingTheme = false)
                _eventFlow.emit(NoteEditorEvent.ShowSnackbar("Failed to analyze mood"))
            }
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            try {
                noteUseCases.addNote(
                    Note(
                        title = _uiState.value.title,
                        content = _uiState.value.content,
                        timestamp = System.currentTimeMillis(),
                        color = _uiState.value.color,
                        themeSeedColor = if (_uiState.value.themeToken != ThemeToken.Default) _uiState.value.themeToken.seedColor else null,
                        themeStyleName = if (_uiState.value.themeToken != ThemeToken.Default) _uiState.value.themeToken.styleName else null,
                        themeLabel = if (_uiState.value.themeToken != ThemeToken.Default) _uiState.value.themeToken.label else null,
                        id = currentNoteId
                    )
                )
                _eventFlow.emit(NoteEditorEvent.SaveNote)
            } catch (e: InvalidNoteException) {
                _eventFlow.emit(
                    NoteEditorEvent.ShowSnackbar(
                        message = e.message ?: "Couldn't save note"
                    )
                )
            }
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            try {
                // If it's a new note that hasn't been saved, just exit.
                // Otherwise, delete from DB.
                if (currentNoteId != null) {
                    val noteToDelete = Note(
                        title = _uiState.value.title,
                        content = _uiState.value.content,
                        timestamp = System.currentTimeMillis(), // timestamp doesn't matter for delete
                        color = _uiState.value.color,
                        id = currentNoteId
                    )
                    noteUseCases.deleteNote(noteToDelete)
                }
                _eventFlow.emit(NoteEditorEvent.SaveNote) // Trigger exit
            } catch (e: Exception) {
                 _eventFlow.emit(
                    NoteEditorEvent.ShowSnackbar(
                        message = "Couldn't delete note"
                    )
                )
            }
        }
    }
}