package com.example.noteapp.feature_note.domain.repository

import com.example.noteapp.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

abstract class NoteRepo {

    abstract fun getAllNotes(): Flow<List<Note>>

    abstract suspend fun getNoteById(id: Int): Note?

    abstract suspend fun insertNote(note: Note)

    //abstract suspend fun updateNote(note: Note)

//    abstract suspend fun deleteNoteById(id: Int)

    abstract suspend fun deleteNote(note: Note)

}