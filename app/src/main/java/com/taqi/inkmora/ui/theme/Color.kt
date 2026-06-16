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