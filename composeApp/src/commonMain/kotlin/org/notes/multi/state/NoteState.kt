package org.notes.multi.state

data class NoteState(

    //Image File Name
    val imageName: String = "",
    //Document File Name
    val fileName: String = "",

    //Note Data
    val uId: Int = 0,
    val title : String = "",
    val document : String = "",
    val text : String = "",

    //Note Draft
    val uIdDraft: Int = 0,
    val titleDraft : String = "",
    val documentByteDraft : List<Byte> = emptyList(),
    val documentExtensionDraft : String = "",
    val textDraft: String = "",

    //Discard BottomSheet
    val bottomSheetDiscard : Boolean = false,

    //Dropdown Image
    val dropDownImage : Boolean = false,

    //Delete Image Confirmation
    val bottomSheetDeleteImage : Boolean = false,
)
