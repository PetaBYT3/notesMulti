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

//Save Image
expect fun saveImage(image: ByteArray): String?

//Delete Image
expect fun deleteImage(image: String)

//Get Image
expect fun getImage(image: String) : Any
