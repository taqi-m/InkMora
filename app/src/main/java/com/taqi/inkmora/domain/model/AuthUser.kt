package com.taqi.inkmora.domain.model

/**
 * Domain model representing an authenticated user.
 * This is decoupled from any specific auth provider (Firebase, Google, etc.).
 */
data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?
)
