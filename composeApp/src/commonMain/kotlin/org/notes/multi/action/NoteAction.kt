package org.notes.multi.action

import org.notes.multi.localdata.database.NotesEntity

sealed interface NoteAction {

    //Selected Notes
    data class SelectedNotes(val uId: Int?) : NoteAction

    //Discard BottomSheet
    data class BottomSheetDiscard(val isShown: Boolean) : NoteAction

    //Dropdown Image
    data class DropDownImage(val isExpanded: Boolean) : NoteAction

    //BottomSheet Delete Image
    data class BottomSheetDeleteImage(val isShown: Boolean) : NoteAction

    //Save Image
    data class SaveImage(val imageBytes: List<Byte>) : NoteAction

    //Delete Image
    data object DeleteImage : NoteAction

    //Title Draft
    data class TitleDraft(val titleDraft: String) : NoteAction

    //Text Draft
    data class TextDraft(val textDraft: String) : NoteAction

    //Save Note
    data object InsertNote : NoteAction
}