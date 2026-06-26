package com.taqi.inkmora.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taqi.inkmora.domain.model.AppMood
import com.taqi.inkmora.ui.theme.*

@Composable
fun OnboardingScreen(
    currentMood: AppMood = AppMood.DEFAULT,
    onMoodSelected: (AppMood) -> Unit = {},
    onOnboardingComplete: () -> Unit
) {
    val animatedPosterBgColor by animateColorAsState(targetValue = MaterialTheme.colorScheme.background, label = "posterMoodBgColor")
    val animatedTextColor by animateColorAsState(targetValue = MaterialTheme.colorScheme.primary, label = "posterTextColor")

    Scaffold(
        containerColor = animatedPosterBgColor
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OnboardingPosterContent(
                currentMood = currentMood,
                onMoodSelected = onMoodSelected,
                animatedTextColor = animatedTextColor,
                onOnboardingComplete = onOnboardingComplete
            )
        }
    }
}

@Composable
private fun OnboardingPosterContent(
    currentMood: AppMood,
    onMoodSelected: (AppMood) -> Unit,
    animatedTextColor: Color,
    onOnboardingComplete: () -> Unit
) {
    val posterHeadlineStyle = TextStyle(
        fontFamily = LoraFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 44.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.5).sp
    )

    val posterSubheadlineStyle = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 28.sp
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
            .padding(top = 96.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        // Top Section: Hero Headline
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            val annotatedHeadline = buildAnnotatedString {
                append("Your thoughts,\nin their true ")
                withStyle(style = SpanStyle(color = animatedTextColor)) {
                    append("colors")
                }
                append(".")
            }
            Text(
                text = annotatedHeadline,
                style = posterHeadlineStyle,
                textAlign = TextAlign.Start
            )
        }

        // Middle Section: Interactive Mood Chips & Subheading
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Tap a mood to preview the canvas:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val moods = listOf(
                    "Calm" to AppMood.CALM,
                    "Energetic" to AppMood.ENERGETIC,
                    "Melancholic" to AppMood.MELANCHOLIC
                )
                moods.forEach { (name, mood) ->
                    val isSelected = currentMood == mood
                    val border = if (isSelected) {
                        BorderStroke(1.5.dp, animatedTextColor)
                    } else {
                        BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    }
                    val chipBg = if (isSelected) {
                        animatedTextColor.copy(alpha = 0.12f)
                    } else {
                        Color.Transparent
                    }

                    Surface(
                        onClick = {
                            onMoodSelected(if (isSelected) AppMood.DEFAULT else mood)
                        },
                        shape = CircleShape,
                        border = border,
                        color = chipBg,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) animatedTextColor else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Text(
                text = "InkMora adapts its canvas to the emotion of your writing. No settings, just ink.",
                style = posterSubheadlineStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start
            )
        }

        // Bottom Section: Primary Action
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onOnboardingComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "Uncap the Pen",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    InkMoraTheme {
        OnboardingScreen {}
    }
}
