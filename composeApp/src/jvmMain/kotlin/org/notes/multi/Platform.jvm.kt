package org.notes.multi

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.notes.multi.localdata.database.Database
import org.notes.multi.utilities.audioPath
import org.notes.multi.utilities.documentPath
import org.notes.multi.utilities.imagePath
import org.notes.multi.utilities.normalizePath
import org.notes.multi.utilities.temp
import org.notes.multi.utilities.videoPath
import java.awt.Desktop
import java.io.ByteArrayOutputStream
import java.io.File
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine

actual val platformModule: Module = module {
    singleOf(::AudioRecorder)
}

//App Theme
@Composable
actual fun isSystemDarkTheme(): Boolean {
    return isSystemInDarkTheme()
}

//Room Database
actual fun getNotesDatabase(): Database {
    val baseDir = File(System.getProperty("user.home"), "NotesMulti")
    val dbFile = File(baseDir, "database/notes.db")
    return Room.databaseBuilder<Database>(
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
        audioPath,
        temp,
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

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class AudioRecorder {
    private var line: TargetDataLine? = null
    private var recordingJob: Job? = null
    private val audioBuffer = ByteArrayOutputStream()

    actual fun startRecording() {
        recordingJob = CoroutineScope(Dispatchers.IO).launch {
            var localLine: TargetDataLine? = null
            try {
                val audioFormat = AudioFormat(44100f, 16, 1, true, false)
                val info = DataLine.Info(TargetDataLine::class.java, audioFormat)

                localLine = (AudioSystem.getLine(info) as TargetDataLine).apply {
                    open(audioFormat)
                    start()
                }

                localLine.let { currentLine ->
                    val buffer = ByteArray(currentLine.bufferSize / 5)

                    while (isActive) {
                        val bytesRead = currentLine.read(buffer, 0, buffer.size)
                        if (bytesRead > 0) {
                            audioBuffer.write(buffer, 0, bytesRead)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                localLine?.stop()
                localLine?.close()
            }
        }
    }

    actual fun stopRecording() : ByteArray? {
        CoroutineScope(Dispatchers.IO).launch {
            recordingJob?.cancelAndJoin()
        }

        line = null
        recordingJob = null

        val audioFile = audioBuffer.toByteArray()
        audioBuffer.reset()

        return if (audioFile.isNotEmpty()) audioFile else null
    }
}