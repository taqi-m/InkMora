package com.taqi.inkmora.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taqi.inkmora.domain.model.ThemeMode
import com.taqi.inkmora.domain.model.ThemeSettings
import com.taqi.inkmora.domain.model.ThemeToken
import com.taqi.inkmora.domain.repository.SettingsRepository
import com.taqi.inkmora.ui.theme.AppThemeState
import com.taqi.inkmora.ui.theme.ThemeCoordinator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    val themeCoordinator: ThemeCoordinator
) : ViewModel() {

    val themeSettings: StateFlow<ThemeSettings> = settingsRepository.getThemeSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeSettings()
        )

    val activeThemeState: StateFlow<AppThemeState> = themeCoordinator.activeThemeState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppThemeState(ThemeToken.Default, ThemeMode.FOLLOW_SYSTEM)
        )

    fun updateThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            settingsRepository.updateThemeMode(themeMode)
        }
    }

    fun updateGlobalTheme(themeToken: ThemeToken) {
        viewModelScope.launch {
            settingsRepository.updateGlobalTheme(themeToken)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            settingsRepository.setOnboardingComplete(true)
        }
    }
}
