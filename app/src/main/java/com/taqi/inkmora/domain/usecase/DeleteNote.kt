package com.taqi.inkmora.domain.usecase

import com.taqi.inkmora.domain.model.Note
import com.taqi.inkmora.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}
