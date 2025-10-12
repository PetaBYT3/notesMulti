package org.notes.multi.action

import org.notes.multi.localdata.database.NotesEntity

sealed interface NoteAction {

    //Selected Notes
    data class SelectedNotes(val note: NotesEntity) : NoteAction

    //Clear Note
    data object ClearNote : NoteAction

    //Discard BottomSheet
    data class ShowDiscardBottomSheet(val showDiscardBottomSheet: Boolean) : NoteAction

    //Enable Title Editing
    data class IsTitleEditEnabled(val isEnabled: Boolean) : NoteAction

    //Title Draft
    data class TitleDraft(val titleDraft: String) : NoteAction

    //Text Draft
    data class TextDraft(val textDraft: String) : NoteAction

    //Save Note
    data object InsertNote : NoteAction
}