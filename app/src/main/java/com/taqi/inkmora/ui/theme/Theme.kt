package com.taqi.inkmora.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.taqi.inkmora.domain.model.ThemeMode
import com.taqi.inkmora.domain.model.ThemeSettings
import com.taqi.inkmora.domain.model.ThemeToken
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle
import androidx.compose.animation.core.tween

@Composable
fun InkMoraTheme(
    themeToken: ThemeToken = ThemeToken.Default,
    themeMode: ThemeMode = ThemeMode.FOLLOW_SYSTEM,
    content: @Composable () -> Unit
) {
    val isDarkTheme = when (themeMode) {
        ThemeMode.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    
    val style = try {
        PaletteStyle.valueOf(themeToken.styleName)
    } catch (e: Exception) {
        PaletteStyle.TonalSpot // Fallback
    }

    DynamicMaterialTheme(
        seedColor = Color(themeToken.seedColor),
        isDark = isDarkTheme,
        style = style,
        animate = true,
        typography = InkMoraTypography,
        shapes = InkMoraShapes
    ) {
        // Edge-to-edge: system bars match the app background
        val colorScheme = MaterialTheme.colorScheme
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window
                val insetsController = WindowCompat.getInsetsController(window, view)
                
                window.statusBarColor = colorScheme.background.toArgb()
                insetsController.isAppearanceLightStatusBars = !isDarkTheme
                
                window.navigationBarColor = colorScheme.background.toArgb()
                insetsController.isAppearanceLightNavigationBars = !isDarkTheme
            }
        }

        content()
    }
}