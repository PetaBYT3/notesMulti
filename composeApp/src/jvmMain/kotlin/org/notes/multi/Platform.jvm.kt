package org.notes.multi

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.notes.multi.localdata.database.NotesDatabase
import java.io.File

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