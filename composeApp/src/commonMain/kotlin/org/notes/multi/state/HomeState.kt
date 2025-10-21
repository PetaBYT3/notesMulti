package org.notes.multi.state

import org.notes.multi.localdata.database.NotesEntity
import org.notes.multi.localdata.database.NotesRelation

data class AllNotesWrapper(
    val note : NotesRelation,
    val isSelected : Boolean = false
)

data class HomeState(

    val isSelectionEnabled : Boolean = false,

    val allNotes: List<AllNotesWrapper> = emptyList(),

    //Delete BottomSheet
    val showDeleteBottomSheet : Boolean = false,
    val noteToDelete : NotesRelation? = null,
)
