package org.notes.multi

import androidx.compose.runtime.Composable
import org.notes.multi.localdata.database.NotesDatabase

//AppTheme
@Composable
expect fun isSystemDarkTheme() : Boolean

//Room Database
expect fun getNotesDatabase(): NotesDatabase

//Create Base Directory
expect fun createBaseDirectory()

//Image File Extension
expect fun saveImage(image: ByteArray): String?
expect fun deleteImage(image: String)
expect fun getImage(image: String) : Any

//Document File Extension
expect fun saveDocument(documentByte: ByteArray, documentExtension: String): String
expect fun getDocument(documentName: String)