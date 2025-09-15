package com.example.noteapp.feature_note.presentstion.add_edit_note

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    object SaveNote : UiEvent()

}