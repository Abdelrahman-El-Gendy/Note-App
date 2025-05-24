package com.example.noteapp.feature_note.domain.usecase

import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.repository.NoteRepo

class GetNoteUseCase(
    private val repository: NoteRepo
) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}