package org.notes.multi.repository

import kotlinx.coroutines.flow.Flow
import org.notes.multi.localdata.database.AppDao
import org.notes.multi.localdata.database.DocumentsEntity
import org.notes.multi.localdata.database.ImageEntity
import org.notes.multi.localdata.database.NotesEntity
import org.notes.multi.localdata.database.NotesRelation

class NotesRepository(
    private val appDao: AppDao
) {

    fun getAllNotes(): Flow<List<NotesRelation>> {
        return appDao.getAllNotes()
    }

    fun getNoteByUid(uId: Long): Flow<NotesRelation> {
        return appDao.getNoteByUid(uId)
    }

    suspend fun upsertNote(note: NotesEntity): Long {
        return appDao.upsertNote(note)
    }

    suspend fun deleteNote(note: NotesEntity) {
        appDao.deleteNote(note)
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
}