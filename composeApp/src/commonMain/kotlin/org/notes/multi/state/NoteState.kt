package org.notes.multi.state

import org.notes.multi.localdata.database.AudioEntity
import org.notes.multi.localdata.database.DocumentsEntity
import org.notes.multi.localdata.database.ImageEntity

data class NoteState(

    //uid
    val uId : Long = 0,

    //Image
    val image : ImageEntity? = null,

    //Document
    val documents : List<DocumentsEntity> = emptyList(),
    val expandDocuments : Boolean = false,
    val documentToDelete : DocumentsEntity? = null,
    val bottomSheetDeleteDocument : Boolean = false,

    val audio : List<AudioEntity> = emptyList(),
    val expandAudio : Boolean = false,
    val audioCountUp : Int = 0,
    val isRecording : Boolean = false,

    //Note Initial
    val initialTitle : String = "",
    val initialText : String = "",

    //Note Draft
    val draftTitle : String = "",
    val draftText : String = "",

    val expandFloatingActionButton: Boolean = false,


    //Discard BottomSheet
    val bottomSheetDiscard : Boolean = false,

    //Dropdown Image
    val dropDownImage : Boolean = false,

    //Show All Documents

    //Delete Image Confirmation
    val bottomSheetDeleteImage : Boolean = false,
)
