package com.example.noteapp.feature_note.presentstion.add_edit_note

import NoteTextFieldState
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.feature_note.domain.model.InvalidNoteException
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.usecase.NoteUseCases
import com.example.noteapp.feature_note.domain.util.ImageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter title..."))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(hint = "Enter some content"))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf<Int>(Color.White.toArgb())
    val noteColor: State<Int> = _noteColor

    private var _currentImagePath = ImageUtils.getDefaultImagePath(context)
    private val _noteImage = mutableStateOf(_currentImagePath)
    val noteImage: State<String> = _noteImage

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null
    var tempCameraUri = mutableStateOf<Uri?>(null)
        private set

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = _noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = _noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                        _currentImagePath =
                            note.imagePath ?: ImageUtils.getDefaultImagePath(context)
                        _noteImage.value = _currentImagePath
                    }
                }
            }
        }
    }

    fun getTemporaryFileUri(context: Context): Uri {
        val tempFile =
            File(context.cacheDir, "camera_photo_${System.currentTimeMillis()}.jpg").apply {
                createNewFile()
            }
        return androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            tempFile
        )
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = _noteTitle.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = _noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = _noteContent.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _noteContent.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }

            is AddEditNoteEvent.ImageSelected -> {
                viewModelScope.launch {
                    event.uri?.let { uri ->
                        val path = ImageUtils.saveImageToInternalStorage(context, uri)
                        if (path != _currentImagePath) {
                            ImageUtils.deleteImage(_currentImagePath)
                            _currentImagePath = path
                            _noteImage.value = path
                        }
                    }
                }
            }

            is AddEditNoteEvent.CameraImageCaptured -> {
                viewModelScope.launch {
                    event.uri?.let { uri ->
                        val path = ImageUtils.saveImageToInternalStorage(context, uri)
                        if (path != _currentImagePath) {
                            ImageUtils.deleteImage(_currentImagePath)
                            _currentImagePath = path
                            _noteImage.value = path
                            tempCameraUri.value = null
                        }
                    }
                }
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = _noteTitle.value.text,
                                content = _noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = _noteColor.value,
                                imagePath = _noteImage.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }


}
