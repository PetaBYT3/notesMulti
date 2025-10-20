package org.notes.multi.state

import org.notes.multi.localdata.database.AudioEntity
import org.notes.multi.localdata.database.DocumentsEntity
import org.notes.multi.localdata.database.ImageEntity

data class NoteState(

    val bottomSheetDiscard : Boolean = false,

    //uid
    val uId : Long = 0,

    //Image
    val image : ImageEntity? = null,
    val bottomSheetDeleteImage : Boolean = false,
    val dropDownImage : Boolean = false,

    //Document
    val documents : List<DocumentsEntity> = emptyList(),
    val expandDocuments : Boolean = false,
    val documentToDelete : DocumentsEntity? = null,
    val bottomSheetDeleteDocument : Boolean = false,

    //Audio
    val audio : List<AudioEntity> = emptyList(),
    val expandAudio : Boolean = false,
    val audioToDelete : AudioEntity? = null,
    val bottomSheetDeleteAudio : Boolean = false,
    val audioCountUp : Int = 0,
    val isRecording : Boolean = false,

    //Note Initial
    val initialTitle : String = "",
    val initialText : String = "",

    //Note Draft
    val draftTitle : String = "",
    val draftText : String = "",

    val expandFloatingActionButton: Boolean = false,
)
