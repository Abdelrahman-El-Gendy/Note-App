package com.example.noteapp.feature_note.domain.usecase

import com.example.noteapp.feature_note.domain.model.InvalidNoteException
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.repository.NoteRepo
import java.io.InvalidClassException
import kotlin.jvm.Throws

class InsertNoteUseCase(
    private val noteRepo: NoteRepo

) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("the note title can not be empty!")
        }
        if (note.content.isBlank()){
            throw InvalidNoteException("the note content can not be empty! ")
        }
        noteRepo.insertNote(note)
    }
}