package com.example.noteapp.feature_note.domain.usecase

import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.repository.NoteRepo
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

class DeleteNoteUseCaseTest {
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var noteRepo: NoteRepo

    @Before
    fun setUp() {
        noteRepo = mock()
        deleteNoteUseCase = DeleteNoteUseCase(noteRepo)
    }

    @Test
    fun `Delete note calls repository delete`() = runBlocking {
        // Given
        val note = Note(
            title = "Test Note",
            content = "Test Content",
            timestamp = 1000L,
            color = 1
        )

        // When
        deleteNoteUseCase(note)

        // Then
        verify(noteRepo).deleteNote(note)
        verifyNoMoreInteractions(noteRepo)
    }
}
