package org.notes.multi

import androidx.compose.runtime.Composable
import org.koin.core.module.Module
import org.notes.multi.localdata.database.Database

//AppTheme
@Composable
expect fun isSystemDarkTheme() : Boolean

//Room Database
expect fun getNotesDatabase() : Database

//Create Base Directory
expect fun createBaseDirectory()

//File Operator
expect fun saveFile(
    targetDir: String,
    fileByte: ByteArray,
    fileName: String,
): String

expect fun getFile(targetDir: String): Any

expect fun openFile(targetDir: String)

expect fun deleteFile(targetDir: String)

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class AudioRecorder {
    fun startRecording()
    fun stopRecording() : ByteArray?
}

expect val platformModule: Module