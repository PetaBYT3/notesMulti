package org.notes.multi

import androidx.compose.runtime.Composable
import org.notes.multi.localdata.database.AppDatabase

//AppTheme
@Composable
expect fun isSystemDarkTheme() : Boolean

//Room Database
expect fun getNotesDatabase() : AppDatabase

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