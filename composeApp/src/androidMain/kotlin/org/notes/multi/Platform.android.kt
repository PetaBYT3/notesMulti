package org.notes.multi

import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.core.content.FileProvider
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.notes.multi.localdata.database.AppDatabase
import org.notes.multi.utilities.audioPath
import org.notes.multi.utilities.documentPath
import org.notes.multi.utilities.imagePath
import org.notes.multi.utilities.normalizePath
import org.notes.multi.utilities.temp
import org.notes.multi.utilities.videoPath
import java.io.File
import java.util.UUID

actual val platformModule: Module = module {
    singleOf(::AudioRecorder)
}

@Composable
actual fun isSystemDarkTheme(): Boolean {
    return isSystemInDarkTheme()
}

//Get Context
private lateinit var applicationContext: Context
fun initAndroidContext(context: Context) {
    applicationContext = context
}

actual fun getNotesDatabase(): AppDatabase {
    val dbFile = applicationContext.getDatabasePath("notes.db")
    return Room.databaseBuilder<AppDatabase>(
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
        imagePath,
        videoPath,
        documentPath,
        audioPath,
        temp
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

actual fun saveFile(
    targetDir: String,
    fileByte: ByteArray,
    fileName: String
): String {
    val rootDir = applicationContext.getExternalFilesDir(null)
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
    val uri = FileProvider.getUriForFile(
        applicationContext,
        "external.provider",
        File(targetDir)
    )
    val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(File(targetDir).extension) ?: "*/*"

    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, mimeType)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    applicationContext.startActivity(intent)
}

actual fun deleteFile(
    targetDir: String
) {
    val fileToDelete = File(targetDir)

    if (fileToDelete.exists()) {
        fileToDelete.delete()
    }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class AudioRecorder {
    private var recorder: MediaRecorder? = null
    private var tempFile: File? = null

    actual fun startRecording() {
        tempFile = File.createTempFile("audio_record", "m4a", applicationContext.cacheDir)
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(applicationContext)
        } else {
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(tempFile?.absolutePath)

            try {
                prepare()
                start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    actual fun stopRecording() : ByteArray? {
        try {
            recorder?.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val bytes = tempFile?.takeIf { it.exists() }?.readBytes()

        tempFile?.delete()
        tempFile = null

        recorder?.release()
        recorder = null

        return bytes
    }
}