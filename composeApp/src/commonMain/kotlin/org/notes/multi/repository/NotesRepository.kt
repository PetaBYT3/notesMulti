package org.notes.multi.repository

import kotlinx.coroutines.flow.Flow
import org.notes.multi.localdata.database.NotesDao
import org.notes.multi.localdata.database.NotesEntity

class NotesRepository(
    private val noteDao: NotesDao
) {

    fun getAllNotes(): Flow<List<NotesEntity>> {
        return noteDao.getAllNotes()
    }

    fun getNoteByUid(uId: Int): Flow<NotesEntity> {
        return noteDao.getNoteByUid(uId = uId)
    }

    suspend fun insertNote(note: NotesEntity) {
        noteDao.insertNote(note)
    }

    suspend fun deleteNote(note: NotesEntity) {
        noteDao.deleteNote(note)
    }
}