package com.taqi.inkmora.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taqi.inkmora.data.local.ThemeSettingsStore
import com.taqi.inkmora.domain.model.AppMood
import com.taqi.inkmora.domain.model.ThemeMode
import com.taqi.inkmora.domain.model.ThemeSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val themeSettingsStore: ThemeSettingsStore
) : ViewModel() {

    val themeSettings: StateFlow<ThemeSettings> = themeSettingsStore.themeSettings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeSettings()
        )

    fun updateThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            themeSettingsStore.updateThemeMode(themeMode)
        }
    }

    fun updateMood(mood: AppMood) {
        viewModelScope.launch {
            themeSettingsStore.updateMood(mood)
        }
    }
}
