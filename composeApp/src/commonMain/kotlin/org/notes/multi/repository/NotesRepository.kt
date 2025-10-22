package org.notes.multi.repository

import kotlinx.coroutines.flow.Flow
import org.notes.multi.localdata.database.DatabaseDao
import org.notes.multi.localdata.database.AudioEntity
import org.notes.multi.localdata.database.DocumentsEntity
import org.notes.multi.localdata.database.ImageEntity
import org.notes.multi.localdata.database.NotesEntity
import org.notes.multi.localdata.database.DatabaseRelation

class NotesRepository(
    private val appDao: DatabaseDao
) {

    fun getAllNotes(): Flow<List<DatabaseRelation>> {
        return appDao.getAllNotes()
    }

    fun getNoteByUid(uId: Long): Flow<DatabaseRelation> {
        return appDao.getNoteByUid(uId)
    }

    suspend fun upsertNote(note: NotesEntity): Long {
        return appDao.upsertNote(note)
    }

    suspend fun deleteNote(note: NotesEntity) {
        appDao.deleteNote(note)
    }

    suspend fun deleteNoteList(noteList: List<NotesEntity>) {
        appDao.deleteNoteList(noteList)
    }

    suspend fun upsertImage(image: ImageEntity) {
        appDao.upsertImage(image)
    }

    suspend fun deleteImage(image: ImageEntity) {
        appDao.deleteImage(image)
    }

    suspend fun upsertDocument(document: DocumentsEntity) {
        appDao.upsertDocument(document)
    }

    suspend fun deleteDocument(document: DocumentsEntity) {
        appDao.deleteDocument(document)
    }

    suspend fun upsertAudio(audio: AudioEntity) {
        appDao.upsertAudio(audio)
    }

    suspend fun deleteAudio(audio: AudioEntity) {
        appDao.deleteAudio(audio)
    }

}