package org.notes.multi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.notes.multi.action.HomeAction
import org.notes.multi.deleteImage
import org.notes.multi.localdata.database.NotesEntity
import org.notes.multi.repository.NotesRepository
import org.notes.multi.state.HomeState

class HomeViewModel(
    private val notesRepository: NotesRepository
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        getAllNotes()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.InsertNotes -> {
                upsertNotes(note = action.newNotes)
            }
            is HomeAction.ShowDeleteBottomSheet -> {
                showDeleteBottomSheet(
                    showDeleteBottomSheet = action.showDeleteBottomSheet,
                    noteToDelete = action.noteToDelete
                )
            }
            HomeAction.DeleteNote -> {
                val noteToDelete = state.value.noteToDelete
                if (noteToDelete != null) {
                    deleteNote(noteToDelete = noteToDelete)
                }
            }
        }
    }

    private fun getAllNotes() {
        viewModelScope.launch {
            notesRepository.getAllNotes().collect { allNotes ->
                _state.update { it.copy(allNotes = allNotes) }

            }
        }
    }

    private fun upsertNotes(note: NotesEntity) {
        viewModelScope.launch {
            notesRepository.upsertNote(note)
        }
    }

    private fun showDeleteBottomSheet(
        showDeleteBottomSheet: Boolean,
        noteToDelete: NotesEntity?
    ) {
        _state.update {
            it.copy(
                showDeleteBottomSheet = showDeleteBottomSheet,
                noteToDelete = noteToDelete
            )
        }
    }

    private fun deleteNote(noteToDelete: NotesEntity) {
        viewModelScope.launch {
            notesRepository.deleteNote(noteToDelete)
        }
        deleteImage(noteToDelete.image)
    }
}