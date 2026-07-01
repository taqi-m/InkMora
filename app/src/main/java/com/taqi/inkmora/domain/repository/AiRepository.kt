package com.taqi.inkmora.domain.repository

import com.taqi.inkmora.domain.model.ThemeToken

interface AiRepository {
    suspend fun analyzeNoteTheme(noteContent: String): Result<ThemeToken>
}
