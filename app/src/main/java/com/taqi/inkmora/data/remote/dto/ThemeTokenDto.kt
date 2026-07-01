package com.taqi.inkmora.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ThemeTokenDto(
    val seedColorHex: String,
    val styleName: String,
    val label: String
)

@Serializable
data class ThemeTokenResponse(
    val themeToken: ThemeTokenDto
)
