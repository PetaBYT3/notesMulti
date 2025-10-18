package org.notes.multi

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.notes.multi.localdata.database.AppDatabase
import org.notes.multi.utilities.documentPath
import org.notes.multi.utilities.imagePath
import org.notes.multi.utilities.normalizePath
import org.notes.multi.utilities.videoPath
import java.awt.Desktop
import java.io.File
import java.util.UUID

//App Theme
@Composable
actual fun isSystemDarkTheme(): Boolean {
    return isSystemInDarkTheme()
}

//Room Database
actual fun getNotesDatabase(): AppDatabase {
    val baseDir = File(System.getProperty("user.home"), "NotesMulti")
    val dbFile = File(baseDir, "database/notes.db")
    return Room.databaseBuilder<AppDatabase>(
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
        imagePath,
        videoPath,
        documentPath,
        "database",
    )

    for (dirName in listDir) {
        val targetDir = File(baseDir, dirName)
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }
    }
}

actual fun saveFile(
    targetDir: String,
    fileByte: ByteArray,
    fileName: String,
): String {
    val rootDir = File(System.getProperty("user.home"), "NotesMulti")
    val baseDir = File(rootDir, targetDir)

    if (!baseDir.exists()) {
        baseDir.mkdirs()
    }

    val saveFile = File(baseDir, fileName)
    saveFile.writeBytes(fileByte)

    return normalizePath(saveFile.absolutePath)
}

actual fun getFile(
    targetDir: String,
): Any {
    val file = File(targetDir)
    return file
}

actual fun openFile(targetDir: String) {
    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().open(File(targetDir))
    }
}

actual fun deleteFile(targetDir: String) {
    val fileToDelete = File(targetDir)

    if (fileToDelete.exists()) {
        fileToDelete.delete()
    }
}