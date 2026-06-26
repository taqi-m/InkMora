package com.taqi.inkmora.ui.theme

import androidx.compose.ui.graphics.Color

// ── InkMora Palette ─────────────────────────────────────────────
// Philosophy: Deep ink tones for focus, soft lavender for creativity,
// warm off-whites for readability. One accent of muted purple — the
// "AI moment" color — used sparingly.

// Neutrals — the canvas
val Ink900 = Color(0xFF0F0F1A)   // near-black, main dark surface
val Ink800 = Color(0xFF1A1A2E)   // dark navy, primary dark bg
val Ink700 = Color(0xFF252542)   // elevated surface in dark mode
val Ink600 = Color(0xFF32325A)   // card/sheet surface in dark mode

// Off-whites — the paper
val Parchment50 = Color(0xFFF5F5F7)  // main light background
val Parchment100 = Color(0xFFEEEEF4)  // subtle card surface
val Parchment200 = Color(0xFFE2E2EC)  // divider / input bg

// Muted purple — the AI accent ("the ink coming alive")
val AuraPurple = Color(0xFFC084FC)  // primary accent, light mode
val AuraPurple80 = Color(0xFFD8A9FD)  // lighter variant, dark mode
val AuraPurple20 = Color(0xFF6D3FA8)  // deeper, for dark surfaces

// Ink stroke — secondary warm tone
val InkStroke = Color(0xFF7C6FA0)  // muted violet, secondary
val InkStroke80 = Color(0xFF9E8FBF)  // lighter variant

// Semantic
val SuccessGreen = Color(0xFF6FCF97)  // accessible analysis complete
val ErrorRose = Color(0xFFEB5757)  // contrast guard failure
val WarningAmber = Color(0xFFF2C94C)  // low contrast warning

// Text
val TextPrimaryDark = Color(0xFFF0F0F8)  // primary text on dark bg
val TextSecondaryDark = Color(0xFFB0AECA)  // secondary/hint on dark
val TextPrimaryLight = Color(0xFF1A1A2E) // primary text on light bg
val TextSecondaryLight = Color(0xFF6B6880) // secondary/hint on light

// Mood Colors
val MoodCalm = Color(0xFFD0E7FF)
val MoodEnergetic = Color(0xFFFFE0B2)
val MoodMelancholic = Color(0xFFE1BEE7)

// ── Calm Mood Scheme Colors ─────────────────────────────────────
val CalmPrimaryLight = Color(0xFF1E88E5)
val CalmPrimaryContainerLight = Color(0xFFE3F2FD)
val CalmBackgroundLight = Color(0xFFF2F5FA)
val CalmSurfaceLight = Color(0xFFF2F5FA)

val CalmPrimaryDark = Color(0xFF90CAF9)
val CalmPrimaryContainerDark = Color(0xFF0D47A1)
val CalmBackgroundDark = Color(0xFF151922)
val CalmSurfaceDark = Color(0xFF1C2230)

// ── Energetic Mood Scheme Colors ────────────────────────────────
val EnergeticPrimaryLight = Color(0xFFF57C00)
val EnergeticPrimaryContainerLight = Color(0xFFFFE0B2)
val EnergeticBackgroundLight = Color(0xFFFFFDF6)
val EnergeticSurfaceLight = Color(0xFFFFFDF6)

val EnergeticPrimaryDark = Color(0xFFFFB74D)
val EnergeticPrimaryContainerDark = Color(0xFFE65100)
val EnergeticBackgroundDark = Color(0xFF1E1B15)
val EnergeticSurfaceDark = Color(0xFF28241C)

// ── Melancholic Mood Scheme Colors ──────────────────────────────
val MelancholicPrimaryLight = Color(0xFF8E24AA)
val MelancholicPrimaryContainerLight = Color(0xFFF3E5F5)
val MelancholicBackgroundLight = Color(0xFFFAF6FC)
val MelancholicSurfaceLight = Color(0xFFFAF6FC)

val MelancholicPrimaryDark = Color(0xFFE1BEE7)
val MelancholicPrimaryContainerDark = Color(0xFF4A148C)
val MelancholicBackgroundDark = Color(0xFF19151C)
val MelancholicSurfaceDark = Color(0xFF231D29)

