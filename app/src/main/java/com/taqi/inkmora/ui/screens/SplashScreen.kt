package com.taqi.inkmora.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement 
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column      
import androidx.compose.foundation.layout.Spacer      
import androidx.compose.foundation.layout.fillMaxSize 
import androidx.compose.foundation.layout.height      
import androidx.compose.foundation.layout.size        
import androidx.compose.material3.MaterialTheme       
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect        
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight       
import androidx.compose.ui.tooling.preview.Preview    
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation      
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.taqi.inkmora.R
import com.taqi.inkmora.ui.theme.InkMoraTheme

@Composable
fun SplashScreen(
    onStartWriting: () -> Unit
) {
    // Load the Lottie composition from the raw resources
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.inkmora_splash))

    // Animate the composition. Play exactly once.
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    // Navigate to the next screen when the animation finishes
    LaunchedEffect(progress) {
        if (progress == 1f) {
            onStartWriting()
        }
    }

    // Animate alpha based on Lottie progress
    val textAlpha by animateFloatAsState(
        targetValue = if (progress > 0.5f) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "textFadeIn"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Lottie Animation (Scaled down)
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(120.dp) // Explicitly sizing it down
            )

            // Subtle spacing between icon and text
            Spacer(modifier = Modifier.height(16.dp))

            // Text Fade-in (using alpha to maintain layout space)
            Text(
                text = "InkMora",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.alpha(textAlpha)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    InkMoraTheme {
        SplashScreen {}
    }
}

