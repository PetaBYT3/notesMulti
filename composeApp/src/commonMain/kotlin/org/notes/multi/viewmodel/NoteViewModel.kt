package org.notes.multi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.notes.multi.AudioRecorder
import org.notes.multi.action.NoteAction
import org.notes.multi.deleteFile
import org.notes.multi.localdata.database.AudioEntity
import org.notes.multi.localdata.database.DocumentsEntity
import org.notes.multi.localdata.database.ImageEntity
import org.notes.multi.localdata.database.NotesEntity
import org.notes.multi.repository.NotesRepository
import org.notes.multi.repository.TimeRepository
import org.notes.multi.saveFile
import org.notes.multi.state.NoteState
import org.notes.multi.utilities.audioPath
import org.notes.multi.utilities.documentPath
import org.notes.multi.utilities.imagePath
import java.util.UUID

class NoteViewModel(
    private val notesRepository: NotesRepository,
    private val audioRecorder: AudioRecorder,
    private val timeRepository: TimeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NoteState())
    val state = _state.asStateFlow()

    fun onAction(action: NoteAction) {
        when (action) {
            is NoteAction.SelectedNotes -> {
                selectedNotes(action.uId)
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
                saveImage(action.imageBytes, action.imageExtension)
            }
            NoteAction.DeleteImage -> {
                deleteImage()
            }
            is NoteAction.TitleDraft -> {
                titleDraft(action.titleDraft)
            }
            is NoteAction.ExpandDocument -> {
                expandDocument(action.isExpanded)
            }
            is NoteAction.BottomSheetDeleteDocument -> {
                bottomSheetDeleteDocument(action.isShown, action.documentToDelete)
            }
            NoteAction.DeleteDocument -> {
                deleteDocument()
            }
            is NoteAction.SaveDocument -> {
                saveDocument(action.documentByte, action.documentName)
            }
            is NoteAction.ExpandAudio -> {
                expandAudio(action.isExpanded)
            }
            is NoteAction.IsAudioRecording -> {
                isAudioRecording()
            }
            is NoteAction.TextDraft -> {
                textDraft(action.textDraft)
            }
            is NoteAction.ExpandFloatingActionButton -> {
                expandFloatingActionButton(action.isExpanded)
            }
            NoteAction.InsertNote -> {
                insertNote()
            }
        }
    }

    private fun selectedNotes(uId: Long?) {
        viewModelScope.launch {
            if (uId != null) {
                notesRepository.getNoteByUid(uId).collect { initialNote ->
                    _state.update { it.copy(
                        uId = initialNote.noteEntity.uId,
                        image = initialNote.image,
                        documents = initialNote.documentsList,
                        audio = initialNote.audioList,
                        initialTitle = initialNote.noteEntity.title,
                        initialText = initialNote.noteEntity.text,
                        draftTitle = initialNote.noteEntity.title,
                        draftText = initialNote.noteEntity.text,
                    ) }
                }
            } else {
                _state.update { it.copy(
                    uId = 0,
                    image = null,
                    documents = emptyList(),
                    audio = emptyList(),
                    initialTitle = "",
                    initialText = "",
                    draftTitle = "",
                    draftText = "",
                ) }
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

    private fun saveImage(
        imageByte: List<Byte>,
        imageExtension: String
    ) {
        val uId = _state.value.uId
        if (uId != 0L) {
            val imageName = "${UUID.randomUUID()}.$imageExtension"
            val imagePath = saveFile(
                targetDir = imagePath,
                fileByte = imageByte.toByteArray(),
                fileName = imageName
            )
            val image = ImageEntity(
                imageName = imageName,
                imagePath = imagePath,
                ownerUid = uId
            )
            viewModelScope.launch {
                notesRepository.upsertImage(image)
            }
        }
    }

    private fun deleteImage() {
        val imageToDelete = _state.value.image
        if (imageToDelete != null) {
            viewModelScope.launch {
                notesRepository.deleteImage(imageToDelete)
            }
            deleteFile(imageToDelete.imagePath)
        }
    }

    private fun titleDraft(title: String) {
        _state.update { it.copy(draftTitle = title) }
    }

    private fun expandDocument(isExpanded: Boolean) {
        _state.update { it.copy(expandDocuments = isExpanded) }
    }

    private fun bottomSheetDeleteDocument(isShown: Boolean, documentToDelete: DocumentsEntity?) {
        _state.update { it.copy(
            bottomSheetDeleteDocument = isShown,
            documentToDelete = documentToDelete
        ) }
    }

    private fun deleteDocument() {
        val documentToDelete = _state.value.documentToDelete
        if (documentToDelete != null) {
            viewModelScope.launch {
                notesRepository.deleteDocument(documentToDelete)
            }
            deleteFile(documentToDelete.documentPath)
        }
    }

    private fun saveDocument(
        documentByte: List<Byte>,
        documentName: String,
    ) {
        val uId = _state.value.uId
        if (uId != 0L) {
            val documentPath = saveFile(
                targetDir = "$documentPath/$uId",
                fileByte = documentByte.toByteArray(),
                fileName = documentName
            )
            val document = DocumentsEntity(
                uId = 0,
                documentPath = documentPath,
                documentName = documentName,
                ownerUid = uId
            )
            viewModelScope.launch {
                notesRepository.upsertDocument(document)
            }
        }
    }

    private fun expandAudio(isExpanded: Boolean) {
        _state.update { it.copy(expandAudio = isExpanded) }
    }

    private fun isAudioRecording() {
        if (_state.value.isRecording) {
            stopRecording()
        } else {
            startRecording()
        }
    }

    private fun startRecording() {
        _state.update { it.copy(isRecording = true) }
        audioRecorder.startRecording()
        viewModelScope.launch {
            while (_state.value.isRecording) {
                delay(1000)
                _state.update { it.copy(audioCountUp = it.audioCountUp + 1) }
            }
            _state.update { it.copy(audioCountUp = 0) }
        }
    }

    private fun stopRecording() {
        _state.update { it.copy(isRecording = false) }
        val uId = _state.value.uId
        val audioFile = audioRecorder.stopRecording()
        val audioName = "Record (${timeRepository.getCurrentDateTime()}).mp3"

        if (audioFile != null && uId != 0L) {
            val audioPath = saveFile(
                targetDir = "$audioPath/$uId",
                fileByte = audioFile,
                fileName = audioName
            )
            val audio = AudioEntity(
                uId = 0,
                audioName = audioName,
                audioPath = audioPath,
                ownerUid = uId
            )
            viewModelScope.launch {
                notesRepository.upsertAudio(audio)
            }
        }
    }

    private fun textDraft(text: String) {
        _state.update { it.copy(draftText = text) }
    }

    private fun expandFloatingActionButton(isExpanded: Boolean) {
        _state.update { it.copy(expandFloatingActionButton = isExpanded) }
    }

    private fun insertNote() {
        val text = _state.value.draftText
        val title = _state.value.draftTitle

        val noteDraft = NotesEntity(
            uId = _state.value.uId,
            title = title,
            text = text,
        )

        viewModelScope.launch {
            val savedNoteUid = notesRepository.upsertNote(note = noteDraft)
            if (savedNoteUid != -1L) {
                _state.update { it.copy(uId = savedNoteUid) }
            }
        }
    }
}