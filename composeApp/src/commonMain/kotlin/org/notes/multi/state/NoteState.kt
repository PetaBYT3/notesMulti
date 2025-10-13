package org.notes.multi.state

import org.notes.multi.localdata.database.NotesEntity

data class NoteState(

    //Image Byte
    val imageByte: List<Byte>? = null,

    //Note Data
    val uId: Int = 0,
    val title : String = "",
    val text : String = "",
    val imagePath: String = "",

    //Note Draft
    val uIdDraft: Int = 0,
    val titleDraft : String = "",
    val textDraft: String = "",
    val imagePathDraft: String = "",

    //Discard BottomSheet
    val showDiscardBottomSheet : Boolean = false,
)
