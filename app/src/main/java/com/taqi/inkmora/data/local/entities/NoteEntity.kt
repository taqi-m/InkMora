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
    @PrimaryKey(autoGenerate = true) val id: Int? = null
) {
    fun toNote(): Note {
        return Note(
            title = title,
            content = content,
            timestamp = timestamp,
            color = color,
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
        id = id
    )
}
