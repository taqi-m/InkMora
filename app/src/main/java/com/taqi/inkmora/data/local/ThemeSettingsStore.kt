package com.taqi.inkmora.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.taqi.inkmora.domain.model.ThemeMode
import com.taqi.inkmora.domain.model.ThemeSettings
import com.taqi.inkmora.domain.model.ThemeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Internal DataStore source for theme settings.
 */
class ThemeSettingsStore(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val GLOBAL_SEED_COLOR = intPreferencesKey("global_seed_color")
        val GLOBAL_THEME_STYLE = stringPreferencesKey("global_theme_style")
        val GLOBAL_THEME_LABEL = stringPreferencesKey("global_theme_label")
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

            val seedColor = preferences[PreferencesKeys.GLOBAL_SEED_COLOR]
            val themeStyle = preferences[PreferencesKeys.GLOBAL_THEME_STYLE]
            val themeLabel = preferences[PreferencesKeys.GLOBAL_THEME_LABEL]
            val isOnboardingComplete = preferences[PreferencesKeys.ONBOARDING_COMPLETE] ?: false

            val globalThemeToken = if (seedColor != null && themeStyle != null) {
                ThemeToken(seedColor, themeStyle, themeLabel)
            } else {
                ThemeToken.Default
            }

            ThemeSettings(
                themeMode = themeMode,
                globalThemeToken = globalThemeToken,
                isOnboardingComplete = isOnboardingComplete
            )
        }

    suspend fun updateThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeMode.name
        }
    }

    suspend fun updateGlobalTheme(themeToken: ThemeToken) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.GLOBAL_SEED_COLOR] = themeToken.seedColor
            preferences[PreferencesKeys.GLOBAL_THEME_STYLE] = themeToken.styleName
            if (themeToken.label != null) {
                preferences[PreferencesKeys.GLOBAL_THEME_LABEL] = themeToken.label
            } else {
                preferences.remove(PreferencesKeys.GLOBAL_THEME_LABEL)
            }
        }
    }

    suspend fun setOnboardingComplete(complete: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETE] = complete
        }
    }
}
