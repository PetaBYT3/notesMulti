package org.notes.multi.state

import org.notes.multi.localdata.database.NotesEntity

data class HomeState(
    val allNotes: List<NotesEntity> = emptyList()
)
