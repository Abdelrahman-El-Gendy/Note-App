package com.example.noteapp.feature_note.domain.usecase

data class NoteUseCases(
    val getAllNotes: GetAllNotesUseCase,
    val deleteNote: DeleteNoteUseCase,
    val addNote: InsertNoteUseCase,
    val getNoteById: GetNoteByIdUseCase,
    val getNote: GetNoteUseCase
)
