package org.notes.multi.localdata.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NotesEntity>>

    @Insert
    suspend fun insertNote(note: NotesEntity)

    @Delete
    suspend fun deleteNote(note: NotesEntity)

}