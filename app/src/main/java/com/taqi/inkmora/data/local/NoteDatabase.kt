package com.taqi.inkmora.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.taqi.inkmora.data.local.entities.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1
)
abstract class NoteDatabase: RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}
