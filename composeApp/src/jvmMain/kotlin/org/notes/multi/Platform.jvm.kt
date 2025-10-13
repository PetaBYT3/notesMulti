package org.notes.multi

import androidx.compose.runtime.Composable
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.notes.multi.localdata.database.NotesDatabase
import java.io.File
import java.util.UUID

//Room Database
actual fun getNotesDatabase(): NotesDatabase {
    val baseDir = File(System.getProperty("user.home"), "NotesMulti")
    val dbFile = File(baseDir, "database/notes.db")
    return Room.databaseBuilder<NotesDatabase>(
        name = dbFile.absolutePath,
    )
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(true)
        .build()
}

//Create Base Directory
actual fun createBaseDirectory() {
    val baseDir = File(System.getProperty("user.home"), "NotesMulti")
    val listDir = listOf(
        "images",
        "videos",
        "documents",
        "database"
    )

    for (dirName in listDir) {
        val targetDir = File(baseDir, dirName)
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }
    }
}

actual fun saveImage(image: ByteArray): String? {
    val baseDir = File(System.getProperty("user.home"), "NotesMulti")
    val fileName = "${UUID.randomUUID()}.jpg"
    val file = File(baseDir, "images")

    val saveImage = File(file, fileName)
    saveImage.writeBytes(image)

    return fileName
}

actual fun getImage(fileName: String): Any {
    val baseDir = File(System.getProperty("user.home"), "NotesMulti")
    val imageFile = File(baseDir, "images/$fileName")
    return imageFile
}