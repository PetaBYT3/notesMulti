package org.notes.multi.state

data class NoteState(

    //Image Byte
    val imageName: String = "",

    //Note Data
    val uId: Int = 0,
    val title : String = "",
    val text : String = "",

    //Note Draft
    val uIdDraft: Int = 0,
    val titleDraft : String = "",
    val textDraft: String = "",

    //Discard BottomSheet
    val bottomSheetDiscard : Boolean = false,

    //Dropdown Image
    val dropDownImage : Boolean = false,

    //Delete Image Confirmation
    val bottomSheetDeleteImage : Boolean = false,
)
