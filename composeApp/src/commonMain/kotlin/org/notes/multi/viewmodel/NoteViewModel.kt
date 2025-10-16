package org.notes.multi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.notes.multi.action.NoteAction
import org.notes.multi.deleteImage
import org.notes.multi.localdata.database.NotesEntity
import org.notes.multi.repository.NotesRepository
import org.notes.multi.saveDocument
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
                putSelectedNotes(action.uId)
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
            is NoteAction.SaveDocument -> {
                documentDraft(action.documentByte, action.documentExtension)
            }
            is NoteAction.TextDraft -> {
                textDraft(action.textDraft)
            }
            NoteAction.InsertNote -> {
                insertNote()
            }
        }
    }

    private fun putSelectedNotes(uId: Int?) {
        viewModelScope.launch {
            if (uId != null) {
                val noteByUid = notesRepository.getNoteByUid(uId).first()
                _state.update {
                    it.copy(
                        uId = noteByUid.uId,
                        imageName = noteByUid.image,
                        title = noteByUid.title,
                        document = noteByUid.document,
                        text = noteByUid.text,
                        uIdDraft = noteByUid.uId,
                        titleDraft = noteByUid.title,
                        textDraft = noteByUid.text,
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        uId = 0,
                        imageName = "",
                        title = "",
                        text = "",
                        uIdDraft = 0,
                        titleDraft = "",
                        textDraft = "",
                    )
                }
            }
        }
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
            image = imageFile.toString(),
            document = _state.value.fileName,
            title = _state.value.titleDraft,
            text = _state.value.textDraft,
        )
        viewModelScope.launch {
            notesRepository.upsertNote(note = noteDraft)
        }
    }

    private fun deleteImage() {
        val noteDraft = NotesEntity(
            uId = _state.value.uIdDraft,
            image = "",
            document = _state.value.fileName,
            title = _state.value.titleDraft,
            text = _state.value.textDraft,
        )
        viewModelScope.launch {
            notesRepository.upsertNote(note = noteDraft)
        }
        deleteImage(_state.value.imageName)
    }

    private fun titleDraft(title: String) {
        _state.update { it.copy(titleDraft = title) }
    }

    private fun documentDraft(
        documentByte: List<Byte>,
        documentExtension: String,
    ) {
        _state.update {
            it.copy(
                documentByteDraft = documentByte,
                documentExtensionDraft = documentExtension,
            )
        }
    }

    private fun textDraft(text: String) {
        _state.update { it.copy(textDraft = text) }
    }

    private fun insertNote() {
        val uIdDraft = _state.value.uIdDraft
        val image = _state.value.imageName
        val file = _state.value.fileName
        val titleDraft = _state.value.titleDraft
        val textDraft = _state.value.textDraft
        _state.update { it.copy(
            uId = uIdDraft,
            imageName = image,
            fileName = file,
            title = titleDraft,
            text = textDraft,
        ) }
        val documentByte = _state.value.documentByteDraft.toByteArray()
        val documentExtension = _state.value.documentExtensionDraft
        val saveDocument = saveDocument(documentByte, documentExtension)
        val noteDraft = NotesEntity(
            uId = uIdDraft,
            image = image,
            document = saveDocument,
            title = titleDraft,
            text = textDraft,
        )
        viewModelScope.launch {
            notesRepository.upsertNote(note = noteDraft)
        }
    }
}