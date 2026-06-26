package com.taqi.inkmora.domain.model

enum class AppMood {
    DEFAULT,
    CALM,
    ENERGETIC,
    MELANCHOLIC
}

enum class ThemeMode {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK
}

data class ThemeSettings(
    val themeMode: ThemeMode = ThemeMode.FOLLOW_SYSTEM,
    val mood: AppMood = AppMood.DEFAULT,
    val isOnboardingComplete: Boolean = false
)
