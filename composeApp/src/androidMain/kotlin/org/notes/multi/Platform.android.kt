package org.notes.multi

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.notes.multi.localdata.database.NotesDatabase
import java.io.File
import java.util.UUID

@Composable
actual fun isSystemDarkTheme(): Boolean {
    return isSystemInDarkTheme()
}

//Get Context
private lateinit var applicationContext: Context
fun initAndroidContext(context: Context) {
    applicationContext = context
}

actual fun getNotesDatabase(): NotesDatabase {
    val dbFile = applicationContext.getDatabasePath("notes.db")
    return Room.databaseBuilder<NotesDatabase>(
        context = applicationContext,
        name = dbFile.absolutePath,
    )
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(true)
        .build()
}

//Create Base Directory
actual fun createBaseDirectory() {
    val baseDir = applicationContext.getExternalFilesDir(null)
    val listDir = listOf(
        "images",
        "videos",
        "documents"
    )

    if (baseDir != null) {
        for (dirName in listDir) {
            val targetDir = File(baseDir, dirName)
            if (!targetDir.exists()) {
                targetDir.mkdirs()
            }
        }
    }
}

actual fun saveImage(image: ByteArray): String? {
    val baseDir = applicationContext.getExternalFilesDir(null)
    val fileName = "${UUID.randomUUID()}.jpg"
    val targetDir = File(baseDir, "images")

    val saveImage = File(targetDir, fileName)
    saveImage.writeBytes(image)

    return fileName
}

actual fun deleteImage(image: String) {
    val baseDir = applicationContext.getExternalFilesDir(null)
    val imageFile = File(baseDir, "images/$image")

    if (imageFile.exists()) {
        imageFile.delete()
    }
}

actual fun getImage(image: String): Any {
    val baseDir = applicationContext.getExternalFilesDir(null)
    val imageFile = File(baseDir, "images/$image")
    return imageFile
}