package com.example.noteapp.feature_note.domain.usecase

import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.repository.NoteRepo

class DeleteNoteUseCase(
    private val noteRepo: NoteRepo
) {
    suspend operator fun invoke(note: Note) {
        noteRepo.deleteNote(note)
    }
}