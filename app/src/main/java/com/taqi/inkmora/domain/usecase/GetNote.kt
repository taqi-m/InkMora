package com.taqi.inkmora.domain.usecase

import com.taqi.inkmora.domain.model.Note
import com.taqi.inkmora.domain.repository.NoteRepository

class GetNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}
