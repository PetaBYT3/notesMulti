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

    suspend fun insertNote(note: NotesEntity) {
        noteDao.insertNote(note)
    }
}