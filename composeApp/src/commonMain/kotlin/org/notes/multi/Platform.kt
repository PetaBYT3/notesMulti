package org.notes.multi

import androidx.compose.runtime.Composable
import org.notes.multi.localdata.database.NotesDatabase

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

//Room Database
expect fun getNotesDatabase(): NotesDatabase

//Save Image
expect suspend fun saveImage(byteArray: ByteArray): String?

//Image Picker