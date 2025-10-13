package org.notes.multi

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import org.notes.multi.localdata.database.NotesDatabase
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.util.UUID
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

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

actual fun getImage(fileName: String): Any {
    val baseDir = applicationContext.getExternalFilesDir(null)
    val imageFile = File(baseDir, "images/$fileName")
    return imageFile
}