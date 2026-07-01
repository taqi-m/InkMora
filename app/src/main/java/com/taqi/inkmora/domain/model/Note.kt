package com.taqi.inkmora.domain.model

data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    val themeSeedColor: Int? = null,
    val themeStyleName: String? = null,
    val themeLabel: String? = null,
    val id: Int? = null
)

class InvalidNoteException(message: String): Exception(message)
