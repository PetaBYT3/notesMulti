package org.notes.multi.localdata.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        NotesEntity::class,
        ImageEntity::class,
        DocumentsEntity::class,
        AudioEntity::class
               ],
    version = 11,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun appDao() : AppDao
}