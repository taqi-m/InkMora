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
import com.taqi.inkmora.domain.model.AppMood
import com.taqi.inkmora.domain.model.ThemeMode
import com.taqi.inkmora.domain.model.ThemeSettings

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

// ── Calm Mood Schemes ──────────────────────────────────────────
val CalmLightColorScheme = lightColorScheme(
    primary = CalmPrimaryLight,
    onPrimary = Parchment50,
    primaryContainer = CalmPrimaryContainerLight,
    onPrimaryContainer = CalmPrimaryLight,

    secondary = InkStroke,
    onSecondary = Parchment50,
    secondaryContainer = Parchment200,
    onSecondaryContainer = TextSecondaryLight,

    tertiary = SuccessGreen,
    onTertiary = Ink900,

    background = CalmBackgroundLight,
    onBackground = TextPrimaryLight,

    surface = CalmSurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Parchment100,
    onSurfaceVariant = TextSecondaryLight,

    outline = Parchment200,
    error = ErrorRose,
    onError = Parchment50,
)

val CalmDarkColorScheme = darkColorScheme(
    primary = CalmPrimaryDark,
    onPrimary = Ink900,
    primaryContainer = CalmPrimaryContainerDark,
    onPrimaryContainer = CalmPrimaryDark,

    secondary = InkStroke80,
    onSecondary = Ink900,
    secondaryContainer = Ink600,
    onSecondaryContainer = TextSecondaryDark,

    tertiary = SuccessGreen,
    onTertiary = Ink900,

    background = CalmBackgroundDark,
    onBackground = TextPrimaryDark,

    surface = CalmSurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = Ink600,
    onSurfaceVariant = TextSecondaryDark,

    outline = InkStroke,
    error = ErrorRose,
    onError = Ink900,
)

// ── Energetic Mood Schemes ───────────────────────────────────────
val EnergeticLightColorScheme = lightColorScheme(
    primary = EnergeticPrimaryLight,
    onPrimary = Parchment50,
    primaryContainer = EnergeticPrimaryContainerLight,
    onPrimaryContainer = EnergeticPrimaryLight,

    secondary = InkStroke,
    onSecondary = Parchment50,
    secondaryContainer = Parchment200,
    onSecondaryContainer = TextSecondaryLight,

    tertiary = SuccessGreen,
    onTertiary = Ink900,

    background = EnergeticBackgroundLight,
    onBackground = TextPrimaryLight,

    surface = EnergeticSurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Parchment100,
    onSurfaceVariant = TextSecondaryLight,

    outline = Parchment200,
    error = ErrorRose,
    onError = Parchment50,
)

val EnergeticDarkColorScheme = darkColorScheme(
    primary = EnergeticPrimaryDark,
    onPrimary = Ink900,
    primaryContainer = EnergeticPrimaryContainerDark,
    onPrimaryContainer = EnergeticPrimaryDark,

    secondary = InkStroke80,
    onSecondary = Ink900,
    secondaryContainer = Ink600,
    onSecondaryContainer = TextSecondaryDark,

    tertiary = SuccessGreen,
    onTertiary = Ink900,

    background = EnergeticBackgroundDark,
    onBackground = TextPrimaryDark,

    surface = EnergeticSurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = Ink600,
    onSurfaceVariant = TextSecondaryDark,

    outline = InkStroke,
    error = ErrorRose,
    onError = Ink900,
)

// ── Melancholic Mood Schemes ─────────────────────────────────────
val MelancholicLightColorScheme = lightColorScheme(
    primary = MelancholicPrimaryLight,
    onPrimary = Parchment50,
    primaryContainer = MelancholicPrimaryContainerLight,
    onPrimaryContainer = MelancholicPrimaryLight,

    secondary = InkStroke,
    onSecondary = Parchment50,
    secondaryContainer = Parchment200,
    onSecondaryContainer = TextSecondaryLight,

    tertiary = SuccessGreen,
    onTertiary = Ink900,

    background = MelancholicBackgroundLight,
    onBackground = TextPrimaryLight,

    surface = MelancholicSurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Parchment100,
    onSurfaceVariant = TextSecondaryLight,

    outline = Parchment200,
    error = ErrorRose,
    onError = Parchment50,
)

val MelancholicDarkColorScheme = darkColorScheme(
    primary = MelancholicPrimaryDark,
    onPrimary = Ink900,
    primaryContainer = MelancholicPrimaryContainerDark,
    onPrimaryContainer = MelancholicPrimaryDark,

    secondary = InkStroke80,
    onSecondary = Ink900,
    secondaryContainer = Ink600,
    onSecondaryContainer = TextSecondaryDark,

    tertiary = SuccessGreen,
    onTertiary = Ink900,

    background = MelancholicBackgroundDark,
    onBackground = TextPrimaryDark,

    surface = MelancholicSurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = Ink600,
    onSurfaceVariant = TextSecondaryDark,

    outline = InkStroke,
    error = ErrorRose,
    onError = Ink900,
)

@Composable
fun InkMoraTheme(
    themeSettings: ThemeSettings = ThemeSettings(),
    darkTheme: Boolean = when (themeSettings.themeMode) {
        ThemeMode.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    },
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

        else -> when (themeSettings.mood) {
            AppMood.DEFAULT -> if (darkTheme) DarkColorScheme else LightColorScheme
            AppMood.CALM -> if (darkTheme) CalmDarkColorScheme else CalmLightColorScheme
            AppMood.ENERGETIC -> if (darkTheme) EnergeticDarkColorScheme else EnergeticLightColorScheme
            AppMood.MELANCHOLIC -> if (darkTheme) MelancholicDarkColorScheme else MelancholicLightColorScheme
        }
    }

    // Edge-to-edge: system bars match the app background
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            
            window.statusBarColor = colorScheme.background.toArgb()
            insetsController.isAppearanceLightStatusBars = !darkTheme
            
            window.navigationBarColor = colorScheme.background.toArgb()
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = InkMoraTypography,
        shapes = InkMoraShapes,
        content = content
    )
}