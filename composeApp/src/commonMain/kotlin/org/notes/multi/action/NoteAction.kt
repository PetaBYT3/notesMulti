package org.notes.multi.action

sealed interface NoteAction {

    //On Page Closed
    data object IsPageClosed : NoteAction

    //Enable Title Editing
    data class IsTitleEditEnabled(val isEnabled: Boolean) : NoteAction

    //Title Draft
    data class TitleDraft(val titleDraft: String) : NoteAction

    //Text Draft
    data class TextDraft(val textDraft: String) : NoteAction
}