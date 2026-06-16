package com.taqi.inkmora.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ── InkMora Shapes ───────────────────────────────────────────────
// Slightly softer than Material defaults — rounded enough to feel
// modern and approachable, but not so bubbly that it loses the
// "refined writing tool" character.
val InkMoraShapes = Shapes(
    // Chips, small badges, input fields
    extraSmall = RoundedCornerShape(6.dp),

    // Buttons, text fields
    small = RoundedCornerShape(10.dp),

    // Cards, note list items
    medium = RoundedCornerShape(16.dp),

    // Bottom sheets, dialogs
    large = RoundedCornerShape(24.dp),

    // Full-screen bottom sheets, FAB
    extraLarge = RoundedCornerShape(28.dp),
)