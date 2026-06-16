package com.taqi.inkmora.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ── Dark Scheme ─────────────────────────────────────────────────
// Deep ink surfaces, aura purple as the living accent
private val DarkColorScheme = darkColorScheme(
    primary = AuraPurple80,       // AI accent — main CTA, FAB
    onPrimary = Ink900,
    primaryContainer = AuraPurple20,       // chip/badge backgrounds
    onPrimaryContainer = AuraPurple80,

    secondary = InkStroke80,        // secondary actions
    onSecondary = Ink900,
    secondaryContainer = Ink600,
    onSecondaryContainer = TextSecondaryDark,

    tertiary = SuccessGreen,       // accessibility guard pass
    onTertiary = Ink900,

    background = Ink800,             // main screen bg
    onBackground = TextPrimaryDark,

    surface = Ink700,             // cards, sheets, dialogs
    onSurface = TextPrimaryDark,
    surfaceVariant = Ink600,             // input fields
    onSurfaceVariant = TextSecondaryDark,

    outline = InkStroke,
    error = ErrorRose,
    onError = Ink900,
)

// ── Light Scheme ─────────────────────────────────────────────────
// Clean paper whites, ink navy for type, muted purple accent
private val LightColorScheme = lightColorScheme(
    primary = AuraPurple,         // AI accent — main CTA, FAB
    onPrimary = Parchment50,
    primaryContainer = Parchment100,
    onPrimaryContainer = AuraPurple20,

    secondary = InkStroke,          // secondary actions
    onSecondary = Parchment50,
    secondaryContainer = Parchment200,
    onSecondaryContainer = TextSecondaryLight,

    tertiary = SuccessGreen,       // accessibility guard pass
    onTertiary = Ink900,

    background = Parchment50,        // main screen bg
    onBackground = TextPrimaryLight,

    surface = Parchment50,        // cards, sheets, dialogs
    onSurface = TextPrimaryLight,
    surfaceVariant = Parchment100,       // input fields
    onSurfaceVariant = TextSecondaryLight,

    outline = Parchment200,
    error = ErrorRose,
    onError = Parchment50,
)

@Composable
fun InkMoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color disabled intentionally — InkMora's identity is its
    // own palette. Dynamic color would override that on Android 12+.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Edge-to-edge: status bar matches the app background
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = InkMoraTypography,
        shapes = InkMoraShapes,
        content = content
    )
}