package com.example.noteapp.feature_note.presentstion.notes

import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.util.NoteOrder

sealed class NoteActions {
    data class Order(val noteOrder: NoteOrder) : NoteActions()
    data class DeleteNote(val note: Note) : NoteActions()
    object RestoreNote : NoteActions()
    object ToggleOrderSection : NoteActions()


}