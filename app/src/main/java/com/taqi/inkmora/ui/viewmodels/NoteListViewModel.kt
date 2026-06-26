package com.taqi.inkmora.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taqi.inkmora.domain.usecase.NoteUseCases
import com.taqi.inkmora.ui.screens.NoteListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val minLoadingDuration = 600L

    private val _internalUiState = MutableStateFlow<NoteListUiState>(NoteListUiState.Loading)
    private val _isTimerRunning = MutableStateFlow(false)

    val uiState: StateFlow<NoteListUiState> = combine(
        _internalUiState,
        _isTimerRunning
    ) { state, isTimerRunning ->
        if (isTimerRunning) NoteListUiState.Loading else state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NoteListUiState.Loading
    )

    init {
        loadNotes()
    }

    private fun loadNotes() {
        // Start the minimum loading timer
        viewModelScope.launch {
            _isTimerRunning.value = true
            delay(minLoadingDuration)
            _isTimerRunning.value = false
        }

        noteUseCases.getNotes()
            .onStart { _internalUiState.value = NoteListUiState.Loading }
            .onEach { notes ->
                if (notes.isEmpty()) {
                    _internalUiState.value = NoteListUiState.Empty
                } else {
                    _internalUiState.value = NoteListUiState.Success(notes)
                }
            }
            .catch { exception ->
                _internalUiState.value = NoteListUiState.Error(exception.message ?: "An unexpected error occurred.")
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
