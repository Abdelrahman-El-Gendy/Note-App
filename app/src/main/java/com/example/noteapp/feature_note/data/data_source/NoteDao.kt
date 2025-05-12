package com.example.noteapp.feature_note.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.noteapp.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Upsert(entity = Note::class)
    suspend fun insertNote(note: Note)

    @Update(entity = Note::class)
    suspend fun updateNote(note: Note)

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun deleteNoteById(id: Int)

    @Delete
    suspend fun deleteNote(note: Note)


}