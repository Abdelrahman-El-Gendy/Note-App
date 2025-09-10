package com.example.noteapp.feature_note.domain.usecase

import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.repository.NoteRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetNoteByIdUseCaseTest {
    private lateinit var getNoteByIdUseCase: GetNoteByIdUseCase
    private lateinit var noteRepo: NoteRepo

    @Before
    fun setUp() {
        noteRepo = mock()
        getNoteByIdUseCase = GetNoteByIdUseCase(noteRepo)
    }

    @Test
    fun `Get existing note by id returns note`() = runBlocking {
        // Given
        val noteId = 1
        val expectedNote = Note(
            id = noteId,
            title = "Test Note",
            content = "Test Content",
            timestamp = 1000L,
            color = 1
        )
        whenever(noteRepo.getNoteById(noteId)).thenReturn(expectedNote)

        // When
        val result = getNoteByIdUseCase(noteId)

        // Then
        assertEquals(expectedNote, result)
    }

    @Test
    fun `Get non-existent note returns null`() = runBlocking {
        // Given
        val noteId = 999
        whenever(noteRepo.getNoteById(noteId)).thenReturn(null)

        // When
        val result = getNoteByIdUseCase(noteId)

        // Then
        assertNull(result)
    }
}
