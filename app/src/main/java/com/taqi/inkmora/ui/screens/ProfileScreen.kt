package com.taqi.inkmora.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.taqi.inkmora.R
import com.taqi.inkmora.domain.model.AuthUser
import com.taqi.inkmora.ui.theme.InkMoraTheme
import com.taqi.inkmora.ui.viewmodels.AuthViewModel

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    val isSigningIn by viewModel.isSigningIn.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current

    ProfileContent(
        user = user,
        isSigningIn = isSigningIn,
        error = error,
        onBack = onBack,
        onSignOut = { viewModel.signOut() },
        onSignIn = { viewModel.signInWithGoogle(context) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileContent(
    user: AuthUser?,
    isSigningIn: Boolean,
    error: String?,
    onBack: () -> Unit,
    onSignOut: () -> Unit,
    onSignIn: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (user != null) {
                        IconButton(onClick = onSignOut) {
                            Icon(Icons.Default.Logout, contentDescription = "Sign Out")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (user != null) {
                AuthenticatedProfile(user = user)
            } else {
                GuestProfile(
                    isSigningIn = isSigningIn,
                    onSignInClick = onSignIn
                )
            }

            error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun AuthenticatedProfile(user: AuthUser) {
    if (user.photoUrl != null) {
        AsyncImage(
            model = user.photoUrl,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = user.displayName ?: "InkMora User",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )

    user.email?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Spacer(modifier = Modifier.height(48.dp))
    
    Text(
        text = "Your thoughts are synced with the ink of the cloud.",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    )
}

@Composable
private fun GuestProfile(
    isSigningIn: Boolean,
    onSignInClick: () -> Unit
) {
    Icon(
        Icons.Default.Person,
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .alpha(0.2f),
        tint = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = "Join the InkMora Ink",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Sign in to sync your notes across devices and keep your inspiration safe.",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        onClick = onSignInClick,
        enabled = !isSigningIn,
        modifier = Modifier.fillMaxWidth(),
        shape = CircleShape
    ) {
        if (isSigningIn) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search), // Using search as placeholder for google G
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign in with Google")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenAuthenticatedPreview() {
    InkMoraTheme {
        ProfileContent(
            user = AuthUser(
                uid = "1",
                email = "taqi@inkmora.com",
                displayName = "Taqi Khokhar",
                photoUrl = null
            ),
            isSigningIn = false,
            error = null,
            onBack = {},
            onSignOut = {},
            onSignIn = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenGuestPreview() {
    InkMoraTheme {
        ProfileContent(
            user = null,
            isSigningIn = false,
            error = null,
            onBack = {},
            onSignOut = {},
            onSignIn = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenSigningInPreview() {
    InkMoraTheme {
        ProfileContent(
            user = null,
            isSigningIn = true,
            error = null,
            onBack = {},
            onSignOut = {},
            onSignIn = {}
        )
    }
}
