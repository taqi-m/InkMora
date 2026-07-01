package com.taqi.inkmora.domain.usecase

import com.taqi.inkmora.domain.model.ThemeToken
import com.taqi.inkmora.domain.repository.AiRepository

class AnalyzeNoteMood(
    private val repository: AiRepository
) {
    suspend operator fun invoke(noteContent: String): Result<ThemeToken> {
        if (noteContent.isBlank()) {
            return Result.success(ThemeToken.Default)
        }
        return repository.analyzeNoteTheme(noteContent)
    }
}
