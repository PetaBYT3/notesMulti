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

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

//Room Database
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
        .build()
}

actual suspend fun saveImage(byteArray: ByteArray): String? {
    return try {
        val fileName = "${UUID.randomUUID()}.jpg"
        val file = File(applicationContext.filesDir, fileName)
        file.writeBytes(byteArray)
        file.absolutePath
    } catch (e: Exception) {
        null
    }
}

//Image Picker