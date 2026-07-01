package com.taqi.inkmora.ui.screens

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taqi.inkmora.domain.model.ThemeToken
import com.taqi.inkmora.ui.components.GoogleSignInButton
import com.taqi.inkmora.ui.theme.InkMoraTheme
import com.taqi.inkmora.ui.theme.InterFontFamily
import com.taqi.inkmora.ui.theme.LoraFontFamily

@Composable
fun OnboardingScreen(
    currentMood: ThemeToken = ThemeToken.Default,
    onMoodSelected: (ThemeToken) -> Unit = {},
    onGoogleSignIn: (Context) -> Unit = {},
    isSigningIn: Boolean = false,
    onOnboardingComplete: () -> Unit
) {
    val animatedPosterBgColor by animateColorAsState(targetValue = MaterialTheme.colorScheme.background, label = "posterMoodBgColor")
    val animatedTextColor by animateColorAsState(targetValue = MaterialTheme.colorScheme.primary, label = "posterTextColor")
    val context = LocalContext.current

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
                onGoogleSignIn = { onGoogleSignIn(context) },
                isSigningIn = isSigningIn,
                animatedTextColor = animatedTextColor,
                onOnboardingComplete = onOnboardingComplete
            )
        }
    }
}

@Composable
private fun OnboardingPosterContent(
    currentMood: ThemeToken,
    onMoodSelected: (ThemeToken) -> Unit,
    onGoogleSignIn: () -> Unit,
    isSigningIn: Boolean,
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
                    "Calm" to ThemeToken.Calm,
                    "Energetic" to ThemeToken.Energetic,
                    "Melancholic" to ThemeToken.Melancholic
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
                            onMoodSelected(if (isSelected) ThemeToken.Default else mood)
                        },
                        shape = MaterialTheme.shapes.small,
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 2.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )


            GoogleSignInButton(
                onClick = onGoogleSignIn,
                loading = isSigningIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                text = "Continue with Google",
            )
            /*OutlinedButton(
                onClick = onGoogleSignIn,
                enabled = !isSigningIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
            )
            {
                if (isSigningIn) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Continue with Google",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }*/

            Button(
                onClick = onOnboardingComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary
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
