package org.notes.multi

import androidx.compose.runtime.Composable
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.notes.multi.localdata.database.NotesDatabase
import java.io.File
import java.util.UUID

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

//Room Database
actual fun getNotesDatabase(): NotesDatabase {
    val dbFile = File(System.getProperty("user.home"), "notes.db")
    return Room.databaseBuilder<NotesDatabase>(
        name = dbFile.absolutePath,
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}

actual suspend fun saveImage(byteArray: ByteArray): String? {
    return try {
        val fileName = "${UUID.randomUUID()}.jpg"
        val file = File(System.getProperty("user.home"), fileName)
        file.writeBytes(byteArray)
        file.absolutePath
    } catch (e: Exception) {
        null
    }
}

//Image Picker