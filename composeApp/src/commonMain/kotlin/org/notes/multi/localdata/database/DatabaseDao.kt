package org.notes.multi.localdata.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {

    @Transaction
    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<DatabaseRelation>>

    @Transaction
    @Query("SELECT * FROM notes WHERE uId = :uId")
    fun getNoteByUid(uId: Long): Flow<DatabaseRelation>

    @Upsert
    suspend fun upsertNote(note: NotesEntity) : Long

    @Delete
    suspend fun deleteNote(note: NotesEntity)

    @Delete
    suspend fun deleteNoteList(note: List<NotesEntity>)

    @Upsert
    suspend fun upsertImage(image: ImageEntity)

    @Delete
    suspend fun deleteImage(image: ImageEntity)

    @Upsert
    suspend fun upsertDocument(document: DocumentsEntity)

    @Delete
    suspend fun deleteDocument(document: DocumentsEntity)

    @Upsert
    suspend fun upsertAudio(audio: AudioEntity)

    @Delete
    suspend fun deleteAudio(audio: AudioEntity)
}