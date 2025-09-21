package com.example.noteapp.feature_note.presentstion.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.usecase.NoteUseCases
import com.example.noteapp.feature_note.domain.util.NoteOrder
import com.example.noteapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                }
            }

            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(noteOrder = event.noteOrder)
            }

            is NotesEvent.RemoveImage -> {
                viewModelScope.launch {
                    try {
                        // Store the image path before removing it
                        val imagePath = event.note.imagePath

                        // Create updated note without image
                        val updatedNote = event.note.copy(imagePath = null)

                        // Update the note in the database
                        noteUseCases.addNote(updatedNote)

                        // Delete the actual image file from storage
                        imagePath?.let { path ->
                            deleteImageFile(path)
                        }

                    } catch (e: Exception) {
                        // Handle error - you might want to show a snackbar or toast
                        // For now, we'll just log it
                        println("Error removing image: ${e.message}")
                    }
                }
            }

            NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }

            NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun deleteImageFile(imagePath: String) {
        try {
            val file = File(imagePath)
            if (file.exists()) {
                val deleted = file.delete()
                if (deleted) {
                    println("Image file deleted successfully: $imagePath")
                } else {
                    println("Failed to delete image file: $imagePath")
                }
            }
        } catch (e: Exception) {
            // Log error but don't fail the operation
            println("Error deleting image file: ${e.message}")
        }
    }

    fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getAllNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes as List<Note>,
                    noteOrder = noteOrder,
                )
            }
            .launchIn(viewModelScope)
    }
}