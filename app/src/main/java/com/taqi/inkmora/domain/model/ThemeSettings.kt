package com.taqi.inkmora.domain.model

enum class ThemeMode {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK
}

data class ThemeToken(
    val seedColor: Int,
    val styleName: String,
    val label: String? = null
) {
    companion object {
        val Default = ThemeToken(0xFF1A1A2E.toInt(), "TonalSpot", "Default") // InkMora Navy
        val Calm = ThemeToken(0xFF81A263.toInt(), "TonalSpot", "Calm")
        val Energetic = ThemeToken(0xFFE76F51.toInt(), "Vibrant", "Energetic")
        val Melancholic = ThemeToken(0xFF4A4E69.toInt(), "Expressive", "Melancholic")
        
        fun presets() = listOf(Calm, Energetic, Melancholic)
    }
}

data class ThemeSettings(
    val themeMode: ThemeMode = ThemeMode.FOLLOW_SYSTEM,
    val globalThemeToken: ThemeToken = ThemeToken.Default,
    val isOnboardingComplete: Boolean = false
)
