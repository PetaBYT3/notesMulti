package org.notes.multi

import androidx.compose.runtime.Composable
import org.notes.multi.localdata.database.NotesDatabase
//Room Database
expect fun getNotesDatabase(): NotesDatabase

//Create Base Directory
expect fun createBaseDirectory()

//Save Image
expect fun saveImage(image: ByteArray): String?

//Get Image
expect fun getImage(fileName: String) : Any
