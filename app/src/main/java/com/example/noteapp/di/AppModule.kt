package com.example.noteapp.di

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import com.example.noteapp.feature_note.data.data_source.NoteDatabase
import com.example.noteapp.feature_note.data.repository.NoteRepoImpl
import com.example.noteapp.feature_note.domain.repository.NoteRepo
import com.example.noteapp.feature_note.domain.usecase.DeleteNoteUseCase
import com.example.noteapp.feature_note.domain.usecase.GetAllNotesUseCase
import com.example.noteapp.feature_note.domain.usecase.GetNoteByIdUseCase
import com.example.noteapp.feature_note.domain.usecase.GetNoteUseCase
import com.example.noteapp.feature_note.domain.usecase.InsertNoteUseCase
import com.example.noteapp.feature_note.domain.usecase.NoteUseCases
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
    fun providesNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providesNoteRepo(db: NoteDatabase): NoteRepo {
        return NoteRepoImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun providesNoteUseCase(repo: NoteRepo): NoteUseCases {
        return NoteUseCases(
            getAllNotes = GetAllNotesUseCase(repo),
            deleteNote = DeleteNoteUseCase(repo),
            addNote = InsertNoteUseCase(repo),
            getNoteById = GetNoteByIdUseCase(repo),
            getNote = GetNoteUseCase(repo)
        )
    }


}