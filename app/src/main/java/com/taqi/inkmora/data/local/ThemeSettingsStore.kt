package com.taqi.inkmora.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.taqi.inkmora.domain.model.AppMood
import com.taqi.inkmora.domain.model.ThemeMode
import com.taqi.inkmora.domain.model.ThemeSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Internal DataStore source for theme settings.
 * Refactored to accept DataStore<Preferences> directly for easier unit testing.
 */
class ThemeSettingsStore(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val MOOD = stringPreferencesKey("mood")
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
    }

    val themeSettings: Flow<ThemeSettings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val themeModeString = preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.FOLLOW_SYSTEM.name
            val themeMode = try {
                ThemeMode.valueOf(themeModeString)
            } catch (e: IllegalArgumentException) {
                ThemeMode.FOLLOW_SYSTEM
            }

            val moodString = preferences[PreferencesKeys.MOOD] ?: AppMood.DEFAULT.name
            val mood = try {
                AppMood.valueOf(moodString)
            } catch (e: IllegalArgumentException) {
                AppMood.DEFAULT
            }

            val isOnboardingComplete = preferences[PreferencesKeys.ONBOARDING_COMPLETE] ?: false

            ThemeSettings(themeMode, mood, isOnboardingComplete)
        }

    suspend fun updateThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeMode.name
        }
    }

    suspend fun updateMood(mood: AppMood) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MOOD] = mood.name
        }
    }

    suspend fun setOnboardingComplete(complete: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETE] = complete
        }
    }
}
