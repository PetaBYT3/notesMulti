package org.notes.multi.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.notes.multi.localdata.database.NotesDatabase

fun getNotesDatabase(context: Context): NotesDatabase {
    val dbFile = context.getDatabasePath("notes.db").toString()
    return Room.databaseBuilder<NotesDatabase>(
        context = context.applicationContext,
        name = dbFile
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}