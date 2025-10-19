package org.notes.multi.action

import org.notes.multi.localdata.database.DocumentsEntity

sealed interface NoteAction {

    //Selected Notes
    data class SelectedNotes(val uId: Long?) : NoteAction

    //Discard BottomSheet
    data class BottomSheetDiscard(val isShown: Boolean) : NoteAction

    //Dropdown Image
    data class DropDownImage(val isExpanded: Boolean) : NoteAction

    //BottomSheet Delete Image
    data class BottomSheetDeleteImage(val isShown: Boolean) : NoteAction

    //Save Image
    data class SaveImage(val imageBytes: List<Byte>, val imageExtension: String) : NoteAction

    //Delete Image
    data object DeleteImage : NoteAction

    //Title Draft
    data class TitleDraft(val titleDraft: String) : NoteAction

    //Document
    data class SaveDocument(val documentByte: List<Byte>, val documentName: String) : NoteAction

    //Text Draft
    data class TextDraft(val textDraft: String) : NoteAction

    //Save Note
    data object InsertNote : NoteAction

    //Data State
    data class ExpandDocument(val isExpanded: Boolean) : NoteAction
    data class BottomSheetDeleteDocument(val isShown: Boolean, val documentToDelete: DocumentsEntity?) : NoteAction
    data object DeleteDocument : NoteAction

    data class ExpandFloatingActionButton(val isExpanded: Boolean) : NoteAction
    data class ExpandAudio(val isExpanded: Boolean) : NoteAction
    data object IsAudioRecording : NoteAction
}