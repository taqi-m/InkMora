package com.taqi.inkmora.domain.repository

import android.content.Context
import com.taqi.inkmora.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

/**
 * Interface for authentication operations.
 * Following the Dependency Inversion Principle, the domain layer depends on this interface,
 * while the data layer provides the implementation (e.g., using Firebase).
 */
interface AuthRepository {
    /**
     * Observes the current authentication state.
     * Emits the current [AuthUser] or null if the user is signed out.
     */
    val currentUser: Flow<AuthUser?>

    /**
     * Signs in using Google. The implementation handles token retrieval and Firebase exchange.
     * @param context The [Context] (should be an Activity context) to launch the credential selector.
     * @return A [Result] containing the [AuthUser] on success.
     */
    suspend fun signInWithGoogle(context: Context): Result<AuthUser>

    /**
     * Signs out the current user.
     * @return A [Result] indicating success or failure.
     */
    suspend fun signOut(): Result<Unit>
}
