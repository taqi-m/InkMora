package com.taqi.inkmora.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taqi.inkmora.domain.model.Note

@Entity(tableName = "note")
data class NoteEntity(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    val themeSeedColor: Int? = null,
    val themeStyleName: String? = null,
    val themeLabel: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
) {
    fun toNote(): Note {
        return Note(
            title = title,
            content = content,
            timestamp = timestamp,
            color = color,
            themeSeedColor = themeSeedColor,
            themeStyleName = themeStyleName,
            themeLabel = themeLabel,
            id = id
        )
    }
}

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        title = title,
        content = content,
        timestamp = timestamp,
        color = color,
        themeSeedColor = themeSeedColor,
        themeStyleName = themeStyleName,
        themeLabel = themeLabel,
        id = id
    )
}
