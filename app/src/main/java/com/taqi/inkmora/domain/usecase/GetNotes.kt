package com.taqi.inkmora.domain.usecase

import com.taqi.inkmora.domain.model.Note
import com.taqi.inkmora.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            notes.sortedByDescending { it.timestamp }
        }
    }
}
