package com.taqi.inkmora.data.repository

import com.taqi.inkmora.data.local.ThemeSettingsStore
import com.taqi.inkmora.domain.model.ThemeMode
import com.taqi.inkmora.domain.model.ThemeSettings
import com.taqi.inkmora.domain.model.ThemeToken
import com.taqi.inkmora.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of [SettingsRepository] using Jetpack DataStore.
 * This class maps domain-level calls to the internal [ThemeSettingsStore] data source.
 */
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val themeSettingsStore: ThemeSettingsStore
) : SettingsRepository {

    override fun getThemeSettings(): Flow<ThemeSettings> {
        return themeSettingsStore.themeSettings
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        themeSettingsStore.updateThemeMode(themeMode)
    }

    override suspend fun updateGlobalTheme(themeToken: ThemeToken) {
        themeSettingsStore.updateGlobalTheme(themeToken)
    }

    override suspend fun setOnboardingComplete(complete: Boolean) {
        themeSettingsStore.setOnboardingComplete(complete)
    }
}
