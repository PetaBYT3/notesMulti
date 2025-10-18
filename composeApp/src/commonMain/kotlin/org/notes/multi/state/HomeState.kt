package org.notes.multi.state

import org.notes.multi.localdata.database.NotesEntity
import org.notes.multi.localdata.database.NotesRelation

data class HomeState(
    val allNotes: List<NotesRelation> = emptyList(),

    //Delete BottomSheet
    val showDeleteBottomSheet : Boolean = false,
    val noteToDelete : NotesRelation? = null,
)
