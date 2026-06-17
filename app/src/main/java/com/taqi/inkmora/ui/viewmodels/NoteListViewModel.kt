package com.taqi.inkmora.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taqi.inkmora.domain.usecase.NoteUseCases
import com.taqi.inkmora.ui.screens.NoteListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<NoteListUiState>(NoteListUiState.Loading)
    val uiState: StateFlow<NoteListUiState> = _uiState.asStateFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        noteUseCases.getNotes()
            .onStart { _uiState.value = NoteListUiState.Loading }
            .onEach { notes ->
                if (notes.isEmpty()) {
                    _uiState.value = NoteListUiState.Empty
                } else {
                    _uiState.value = NoteListUiState.Success(notes)
                }
            }
            .catch { exception ->
                _uiState.value = NoteListUiState.Error(exception.message ?: "An unexpected error occurred.")
            }
            .launchIn(viewModelScope)
    }

    // Optional: Expose an event for manual retry if needed
    fun retry() {
        loadNotes()
    }

    fun deleteNote(note: com.taqi.inkmora.domain.model.Note) {
        viewModelScope.launch {
            try {
                noteUseCases.deleteNote(note)
            } catch (e: Exception) {
                // In a production app, you might want to show a transient error (Snackbar)
                // rather than replacing the whole list state.
                // _uiState.value = NoteListUiState.Error("Failed to delete note.")
            }
        }
    }
}
