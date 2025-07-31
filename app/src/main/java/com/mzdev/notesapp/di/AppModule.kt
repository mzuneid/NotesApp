package com.mzdev.notesapp.di

import android.app.Application
import androidx.room.Room
import com.mzdev.notesapp.feature_note.data.local.NoteDatabase
import com.mzdev.notesapp.feature_note.data.repository.NoteRepositoryImpl
import com.mzdev.notesapp.feature_note.domain.repository.NoteRepository
import com.mzdev.notesapp.feature_note.domain.use_cases.AddNoteUseCase
import com.mzdev.notesapp.feature_note.domain.use_cases.DeleteNoteUseCase
import com.mzdev.notesapp.feature_note.domain.use_cases.GetNoteUseCase
import com.mzdev.notesapp.feature_note.domain.use_cases.GetNotesUseCase
import com.mzdev.notesapp.feature_note.domain.use_cases.NoteUseCases
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
    fun provideNoteDatabase(app: Application): NoteDatabase =
        Room
            .databaseBuilder(
                app,
                NoteDatabase::class.java,
                NoteDatabase.DATABASE_NAME,
            ).build()

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository = NoteRepositoryImpl(db.noteDao)

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases =
        NoteUseCases(
            getNotes = GetNotesUseCase(repository),
            deleteNote = DeleteNoteUseCase(repository),
            addNote = AddNoteUseCase(repository),
            getNote = GetNoteUseCase(repository),
        )
}
