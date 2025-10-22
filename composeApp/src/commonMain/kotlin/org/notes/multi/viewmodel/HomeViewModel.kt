package org.notes.multi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.notes.multi.action.HomeAction
import org.notes.multi.localdata.database.DatabaseRelation
import org.notes.multi.repository.NotesRepository
import org.notes.multi.state.AllNotesWrapper
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
            is HomeAction.IsSelectionEnabled -> {
                isSelectionEnabled(action.isEnabled)
            }
            is HomeAction.SelectNote -> {
                selectNote(action.uId, action.isSelect)
            }
            is HomeAction.ShowDeleteBottomSheet -> {
                showDeleteBottomSheet(
                    showDeleteBottomSheet = action.showDeleteBottomSheet,
                    noteToDelete = action.noteToDelete
                )
            }
            HomeAction.DeleteNote -> {
                deleteNote()
            }
            is HomeAction.ShowDeleteListBottomSheet -> {
                showDeleteListBottomSheet()
            }
            HomeAction.DeleteListNote -> {
                deleteListNote()
            }
        }
    }

    private fun isSelectionEnabled(isEnabled : Boolean) {
        if (isEnabled) {
            _state.update { it.copy(isSelectionEnabled = true) }
        } else {
            _state.update {
                val updatedNotes = it.allNotes.map { note ->
                    note.copy(isSelected = false)
                }
                it.copy(allNotes = updatedNotes, isSelectionEnabled = false)
            }
        }
    }

    private fun selectNote(uId: Long, isSelect: Boolean) {
        _state.update {
            val updatedNotes = it.allNotes.map { note ->
                if (note.note.noteEntity.uId == uId) {
                    note.copy(isSelected = isSelect)
                } else {
                    note
                }
            }
            it.copy(allNotes = updatedNotes)
        }
    }

    private fun getAllNotes() {
        viewModelScope.launch {
            notesRepository.getAllNotes().collect { allNotes ->
                val userData: List<AllNotesWrapper> = allNotes.map { note ->
                    AllNotesWrapper(
                        note = note,
                    )
                }
                _state.update { it.copy(allNotes = userData)}
            }
        }
    }

    private fun showDeleteBottomSheet(
        showDeleteBottomSheet: Boolean,
        noteToDelete: DatabaseRelation?
    ) {
        _state.update {
            it.copy(
                showDeleteBottomSheet = showDeleteBottomSheet,
                noteToDelete = noteToDelete
            )
        }
    }

    private fun deleteNote() {
        viewModelScope.launch {
            val noteToDelete = state.value.noteToDelete
            if (noteToDelete != null) {
                notesRepository.deleteNote(noteToDelete.noteEntity)
            }
        }
    }

    private fun showDeleteListBottomSheet() {
        _state.update { it.copy(showDeleteListBottomSheet = !it.showDeleteListBottomSheet) }
    }

    private fun deleteListNote() {
        viewModelScope.launch {
            val selectedNotes = state.value.allNotes.filter { it.isSelected }
            notesRepository.deleteNoteList(selectedNotes.map { it.note.noteEntity })
        }
    }
}