package com.example.noteapp.feature_note.domain.usecase

import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.repository.NoteRepo
import com.example.noteapp.feature_note.domain.util.NoteOrder
import com.example.noteapp.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllNotesUseCase(
    private val noteRepo: NoteRepo
) {
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note?>> {
        return noteRepo.getAllNotes().map { notes ->
            when (noteOrder.orderType) {
                OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it?.title.toString().lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it?.timestamp }
                        is NoteOrder.Color -> notes.sortedBy { it?.color }
                    }
                }

                OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it?.title.toString().lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it?.timestamp }
                        is NoteOrder.Color -> notes.sortedByDescending { it?.color }

                    }
                }
            }
        }
    }
}