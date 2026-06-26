package com.taqi.inkmora.data.remote

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Client for handling Google Sign-In using the modern Credential Manager API.
 */
@Singleton
class GoogleAuthClient @Inject constructor() {

    /**
     * Triggers the Google Sign-In flow and returns the ID token.
     * @param context The [Context] (should be an Activity context) to launch the credential selector.
     * @param webClientId The server's client ID for Google Sign-In (found in Firebase console).
     */
    suspend fun getGoogleIdToken(context: Context, webClientId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val credentialManager = CredentialManager.create(context)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result: GetCredentialResponse = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                Result.success(googleIdTokenCredential.idToken)
            } else {
                Result.failure(Exception("Invalid credential type received: ${credential.type}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
