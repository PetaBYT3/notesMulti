package org.notes.multi.localdata.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NotesEntity>>

    @Query("SELECT * FROM notes WHERE uId = :uId")
    fun getNoteByUid(uId: Int): Flow<NotesEntity>

    @Upsert
    suspend fun upsertNote(note: NotesEntity)

    @Delete
    suspend fun deleteNote(note: NotesEntity)
}