package com.taqi.inkmora.data.repository

import android.graphics.Color
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.PublicPreviewAPI
import com.taqi.inkmora.data.remote.dto.ThemeTokenResponse
import com.taqi.inkmora.domain.model.ThemeToken
import com.taqi.inkmora.domain.repository.AiRepository
import kotlinx.serialization.json.Json
import androidx.core.graphics.toColorInt

class AiRepositoryImpl : AiRepository {

    private val json = Json { ignoreUnknownKeys = true }

    @OptIn(PublicPreviewAPI::class)
    override suspend fun analyzeNoteTheme(noteContent: String): Result<ThemeToken> {
        return try {
            val model = Firebase.ai(backend = GenerativeBackend.googleAI()).templateGenerativeModel()
            
            val response = model.generateContent(
                templateId = "inkmora-theme-generator",
                inputs = mapOf("noteContent" to noteContent)
            )
            
            val responseText = response.text ?: throw IllegalStateException("Empty response from AI")
            val tokenResponse = json.decodeFromString<ThemeTokenResponse>(responseText)
            
            val colorInt = tokenResponse.themeToken.seedColorHex.toColorInt()
            
            Result.success(
                ThemeToken(
                    seedColor = colorInt,
                    styleName = tokenResponse.themeToken.styleName,
                    label = tokenResponse.themeToken.label
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
