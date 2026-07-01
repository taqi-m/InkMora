package com.taqi.inkmora.ui.theme

import com.taqi.inkmora.domain.model.ThemeMode
import com.taqi.inkmora.domain.model.ThemeSettings
import com.taqi.inkmora.domain.model.ThemeToken
import com.taqi.inkmora.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

data class AppThemeState(
    val themeToken: ThemeToken,
    val themeMode: ThemeMode
)

@Singleton
class ThemeCoordinator @Inject constructor(
    settingsRepository: SettingsRepository
) {
    private val _activeNoteTheme = MutableStateFlow<ThemeToken?>(null)
    
    /**
     * Set this to a Note's ThemeToken when a note is opened.
     * Set it to null when leaving the note, to revert to the global theme.
     */
    fun setActiveNoteTheme(token: ThemeToken?) {
        _activeNoteTheme.value = token
    }

    /**
     * The single source of truth for the UI.
     * It merges the Global DataStore Settings and the Active Note Override.
     */
    val activeThemeState: Flow<AppThemeState> = combine(
        settingsRepository.getThemeSettings(),
        _activeNoteTheme.asStateFlow()
    ) { globalSettings, activeNoteTheme ->
        
        // If a note is open and has a theme, it overrides the global theme.
        val resolvedToken = activeNoteTheme ?: globalSettings.globalThemeToken

        AppThemeState(
            themeToken = resolvedToken,
            themeMode = globalSettings.themeMode
        )
    }
}
