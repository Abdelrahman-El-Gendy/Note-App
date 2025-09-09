package com.example.noteapp.feature_note.presentstion.add_edit_note

import android.net.Uri
import androidx.compose.ui.focus.FocusState

sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String) : AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent()
    data class EnteredContent(val value: String) : AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent()
    data class ChangeColor(val color: Int) : AddEditNoteEvent()
    data class ImageSelected(val uri: Uri?) : AddEditNoteEvent()
    data class CameraImageCaptured(val uri: Uri?) : AddEditNoteEvent()
    object SaveNote : AddEditNoteEvent()
}
