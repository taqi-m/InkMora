package com.taqi.inkmora.ui.screens

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Splash : Screen()

    @Serializable
    data object NoteList : Screen()

    @Serializable
    data class NoteEditor(val noteId: Int? = null) : Screen()

    @Serializable
    data object ThemePrompt : Screen()

    @Serializable
    data object Onboarding : Screen()
}
