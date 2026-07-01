package com.taqi.inkmora.di

import android.app.Application
import androidx.room.Room
import com.taqi.inkmora.data.local.NoteDatabase
import com.taqi.inkmora.data.repository.NoteRepositoryImpl
import com.taqi.inkmora.domain.repository.NoteRepository
import com.taqi.inkmora.domain.usecase.AddNote
import com.taqi.inkmora.domain.usecase.DeleteNote
import com.taqi.inkmora.domain.usecase.GetNote
import com.taqi.inkmora.domain.usecase.GetNotes
import com.taqi.inkmora.domain.usecase.AnalyzeNoteMood
import com.taqi.inkmora.domain.repository.AiRepository
import com.taqi.inkmora.domain.usecase.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(
        repository: NoteRepository,
        aiRepository: AiRepository
    ): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository),
            analyzeNoteMood = AnalyzeNoteMood(aiRepository)
        )
    }
}
