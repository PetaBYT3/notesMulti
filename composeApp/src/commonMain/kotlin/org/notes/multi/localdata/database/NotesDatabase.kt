package org.notes.multi.localdata.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NotesEntity::class],
    version = 4,
    exportSchema = false
)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun notesDao(): NotesDao
}