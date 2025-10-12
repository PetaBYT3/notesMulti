package org.notes.multi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.notes.multi.action.NoteAction
import org.notes.multi.localdata.database.NotesEntity
import org.notes.multi.repository.NotesRepository
import org.notes.multi.state.NoteState

class NoteViewModel(
    private val notesRepository: NotesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(NoteState())
    val state = _state.asStateFlow()

    fun onAction(action: NoteAction) {
        when (action) {
            is NoteAction.SelectedNotes -> {
                putSelectedNotes(action.note)
            }
            NoteAction.ClearNote -> {
                clearNote()
            }
            is NoteAction.ShowDiscardBottomSheet -> {
                showDiscardBottomSheet(action.showDiscardBottomSheet)
            }
            is NoteAction.IsTitleEditEnabled -> {
                isTitleEditEnabled(action.isEnabled)
            }
            is NoteAction.TitleDraft -> {
                titleDraft(action.titleDraft)
            }
            is NoteAction.TextDraft -> {
                textDraft(action.textDraft)
            }
            NoteAction.InsertNote -> {
                insertNote()
            }
        }
    }

    private fun putSelectedNotes(note: NotesEntity) {
        viewModelScope.launch {
            notesRepository.getNoteByUid(note.uId).collect { noteByUid ->
                _state.update {
                    it.copy(
                        uId = noteByUid.uId,
                        title = noteByUid.title,
                        text = noteByUid.text
                    )
                }
            }
        }
        _state.update { it.copy(
            uIdDraft = note.uId,
            titleDraft = note.title,
            textDraft = note.text
        ) }
    }

    private fun clearNote() {
        _state.update { it.copy(
            uId = 0,
            title = "",
            text = "",
            uIdDraft = 0,
            titleDraft = "",
            textDraft = "",
            isTitleEditingEnabled = false
        ) }
    }

    private fun showDiscardBottomSheet(isShown: Boolean) {
        _state.update { it.copy(showDiscardBottomSheet = isShown) }
    }

    private fun isTitleEditEnabled(isEnabled: Boolean) {
        _state.update { it.copy(isTitleEditingEnabled = isEnabled) }
    }

    private fun titleDraft(title: String) {
        _state.update { it.copy(titleDraft = title) }
    }
    private fun textDraft(text: String) {
        _state.update { it.copy(textDraft = text) }
    }

    private fun insertNote() {
        val uIdDraft = _state.value.uIdDraft
        val titleDraft = _state.value.titleDraft
        val textDraft = _state.value.textDraft
        _state.update { it.copy(
            uId = uIdDraft,
            title = titleDraft,
            text = textDraft
        ) }
        val noteDraft = NotesEntity(
            uId = uIdDraft,
            title = titleDraft,
            text = textDraft
        )
        viewModelScope.launch {
            notesRepository.insertNote(note = noteDraft)
        }
    }
}