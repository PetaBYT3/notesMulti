package org.notes.multi.state

import org.notes.multi.localdata.database.NotesEntity

data class NoteState(

    //Note Data
    val uId: Int = 0,
    val title : String = "",
    val text : String = "",

    //Note Draft
    val uIdDraft: Int = 0,
    val titleDraft : String = "",
    val textDraft: String = "",

    //Discard BottomSheet
    val showDiscardBottomSheet : Boolean = false,

    //Is Title Editing Enabled
    val isTitleEditingEnabled : Boolean = false,
)
