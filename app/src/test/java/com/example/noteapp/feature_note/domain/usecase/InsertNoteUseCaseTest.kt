package com.example.noteapp.feature_note.domain.usecase

import com.example.noteapp.feature_note.domain.model.InvalidNoteException
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.repository.NoteRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

class InsertNoteUseCaseTest {
    private lateinit var insertNoteUseCase: InsertNoteUseCase
    private lateinit var noteRepo: NoteRepo

    @Before
    fun setUp() {
        noteRepo = mock()
        insertNoteUseCase = InsertNoteUseCase(noteRepo)
    }

    @Test
    fun `Insert valid note calls repository insert`() = runBlocking {
        // Given
        val note = Note(
            title = "Test Note",
            content = "Test Content",
            timestamp = 1000L,
            color = 1
        )

        // When
        insertNoteUseCase(note)

        // Then
        verify(noteRepo).insertNote(note)
        verifyNoMoreInteractions(noteRepo)
    }

    @Test
    fun `Insert note with empty title throws InvalidNoteException`() = runBlocking {
        // Given
        val note = Note(
            title = "",
            content = "Test Content",
            timestamp = 1000L,
            color = 1
        )

        try {
            // When
            insertNoteUseCase(note)
            fail("Expected InvalidNoteException")
        } catch (e: InvalidNoteException) {
            // Then
            assertEquals("the note title can not be empty!", e.message)
        }
    }

    @Test
    fun `Insert note with empty content throws InvalidNoteException`() = runBlocking {
        // Given
        val note = Note(
            title = "Test Title",
            content = "",
            timestamp = 1000L,
            color = 1
        )

        try {
            // When
            insertNoteUseCase(note)
            fail("Expected InvalidNoteException")
        } catch (e: InvalidNoteException) {
            // Then
            assertEquals("the note content can not be empty! ", e.message)
        }
    }
}
