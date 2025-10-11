package org.notes.multi.state

import org.notes.multi.localdata.database.NotesEntity

data class NoteState(

    //Note Data
    val note : NotesEntity? = null,

    //Note Draft
    val titleDraft : String? = null,
    val textDraft: String? = null,

    //Discard BottomSheet
    val showDiscardBottomSheet : Boolean = false,

    //Is Title Editing Enabled
    val isTitleEditingEnabled : Boolean = false,
)
