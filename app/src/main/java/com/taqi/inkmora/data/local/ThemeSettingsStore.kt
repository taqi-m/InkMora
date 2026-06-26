package com.taqi.inkmora.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.taqi.inkmora.domain.model.AppMood
import com.taqi.inkmora.domain.model.ThemeMode
import com.taqi.inkmora.domain.model.ThemeSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeSettingsStore(private val context: Context) {

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val MOOD = stringPreferencesKey("mood")
    }

    val themeSettings: Flow<ThemeSettings> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(androidx.datastore.preferences.core.emptyPreferences())
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

            ThemeSettings(themeMode, mood)
        }

    suspend fun updateThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeMode.name
        }
    }

    suspend fun updateMood(mood: AppMood) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.MOOD] = mood.name
        }
    }
}
