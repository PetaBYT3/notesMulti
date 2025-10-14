package org.notes.multi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.notes.multi.action.NoteAction
import org.notes.multi.deleteImage
import org.notes.multi.localdata.database.NotesEntity
import org.notes.multi.repository.NotesRepository
import org.notes.multi.saveImage
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
            is NoteAction.BottomSheetDiscard -> {
                bottomSheetDiscard(action.isShown)
            }
            is NoteAction.DropDownImage -> {
                dropDownImage(action.isExpanded)
            }
            is NoteAction.BottomSheetDeleteImage -> {
                bottomSheetDeleteImage(action.isShown)
            }
            is NoteAction.SaveImage -> {
                saveImage(action.imageBytes)
            }
            NoteAction.DeleteImage -> {
                deleteImage()
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
                        imageName = noteByUid.image,
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
            imageName = "",
            uId = 0,
            title = "",
            text = "",
            uIdDraft = 0,
            titleDraft = "",
            textDraft = "",
        ) }
    }

    private fun bottomSheetDiscard(isShown: Boolean) {
        _state.update { it.copy(bottomSheetDiscard = isShown) }
    }

    private fun dropDownImage(isExpanded: Boolean) {
        _state.update { it.copy(dropDownImage = isExpanded) }
    }

    private fun bottomSheetDeleteImage(isShown: Boolean) {
        _state.update { it.copy(bottomSheetDeleteImage = isShown) }
    }

    private fun saveImage(imageByte: List<Byte>) {
        val imageFile = saveImage(imageByte.toByteArray())
        val noteDraft = NotesEntity(
            uId = _state.value.uIdDraft,
            title = _state.value.titleDraft,
            text = _state.value.textDraft,
            image = imageFile.toString()
        )
        viewModelScope.launch {
            notesRepository.upsertNote(note = noteDraft)
        }
    }

    private fun deleteImage() {
        val noteDraft = NotesEntity(
            uId = _state.value.uIdDraft,
            title = _state.value.titleDraft,
            text = _state.value.textDraft,
            image = ""
        )
        viewModelScope.launch {
            notesRepository.upsertNote(note = noteDraft)
        }
        deleteImage(_state.value.imageName)
    }

    private fun titleDraft(title: String) {
        _state.update { it.copy(titleDraft = title) }
    }
    private fun textDraft(text: String) {
        _state.update { it.copy(textDraft = text) }
    }

    private fun insertNote() {
        val image = _state.value.imageName
        val uIdDraft = _state.value.uIdDraft
        val titleDraft = _state.value.titleDraft
        val textDraft = _state.value.textDraft
        _state.update { it.copy(
            uId = uIdDraft,
            title = titleDraft,
            text = textDraft,
        ) }
        val noteDraft = NotesEntity(
            uId = uIdDraft,
            title = titleDraft,
            text = textDraft,
            image = image
        )
        viewModelScope.launch {
            notesRepository.upsertNote(note = noteDraft)
        }
    }
}