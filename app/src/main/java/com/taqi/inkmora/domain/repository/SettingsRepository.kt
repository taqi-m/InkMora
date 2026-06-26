package com.taqi.inkmora.domain.repository

import com.taqi.inkmora.domain.model.AppMood
import com.taqi.inkmora.domain.model.ThemeMode
import com.taqi.inkmora.domain.model.ThemeSettings
import kotlinx.coroutines.flow.Flow

/**
 * Interface for application-wide settings and feature flags.
 * This lives in the Domain Layer and has no knowledge of the underlying
 * storage mechanism (Jetpack DataStore, SharedPreferences, etc.).
 */
interface SettingsRepository {
    /**
     * Observes the current theme settings reactively.
     */
    fun getThemeSettings(): Flow<ThemeSettings>

    /**
     * Updates the user's preferred theme mode.
     */
    suspend fun updateThemeMode(themeMode: ThemeMode)

    /**
     * Updates the application's mood (dynamic palette).
     */
    suspend fun updateMood(mood: AppMood)

    /**
     * Persists whether the user has completed the onboarding flow.
     */
    suspend fun setOnboardingComplete(complete: Boolean)
}
