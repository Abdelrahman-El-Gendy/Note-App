package com.example.noteapp.feature_note.domain.usecase

import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.repository.NoteRepo
import com.example.noteapp.feature_note.domain.util.NoteOrder
import com.example.noteapp.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetAllNotesUseCaseTest {
    private lateinit var getAllNotesUseCase: GetAllNotesUseCase
    private lateinit var noteRepo: NoteRepo

    @Before
    fun setUp() {
        noteRepo = mock()
        getAllNotesUseCase = GetAllNotesUseCase(noteRepo)
    }

    @Test
    fun `Order notes by title ascending`() = runBlocking {
        // Given
        val notes = listOf(
            Note("B Note", "Content", 1, 1),
            Note("A Note", "Content", 2, 2),
            Note("C Note", "Content", 3, 3)
        )
        whenever(noteRepo.getAllNotes()).thenReturn(flowOf(notes))

        // When
        val result = getAllNotesUseCase(NoteOrder.Title(OrderType.Ascending)).first()

        // Then
        assertEquals("A Note", result[0]?.title)
        assertEquals("B Note", result[1]?.title)
        assertEquals("C Note", result[2]?.title)
    }

    @Test
    fun `Order notes by date descending`() = runBlocking {
        // Given
        val notes = listOf(
            Note("Note 1", "Content", 1, 1),
            Note("Note 2", "Content", 2, 2),
            Note("Note 3", "Content", 3, 3)
        )
        whenever(noteRepo.getAllNotes()).thenReturn(flowOf(notes))

        // When
        val result = getAllNotesUseCase(NoteOrder.Date(OrderType.Descending)).first()

        // Then
        assertEquals(3L, result[0]?.timestamp)
        assertEquals(2L, result[1]?.timestamp)
        assertEquals(1L, result[2]?.timestamp)
    }

    @Test
    fun `Order notes by color ascending`() = runBlocking {
        // Given
        val notes = listOf(
            Note("Note 1", "Content", 1, 3),
            Note("Note 2", "Content", 2, 1),
            Note("Note 3", "Content", 3, 2)
        )
        whenever(noteRepo.getAllNotes()).thenReturn(flowOf(notes))

        // When
        val result = getAllNotesUseCase(NoteOrder.Color(OrderType.Ascending)).first()

        // Then
        assertEquals(1, result[0]?.color)
        assertEquals(2, result[1]?.color)
        assertEquals(3, result[2]?.color)
    }
}
