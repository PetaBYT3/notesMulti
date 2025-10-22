package org.notes.multi.state

import org.notes.multi.localdata.database.DatabaseRelation

data class AllNotesWrapper(
    val note : DatabaseRelation,
    val isSelected : Boolean = false
)

data class HomeState(

    val isSelectionEnabled : Boolean = false,

    val allNotes: List<AllNotesWrapper> = emptyList(),

    //Delete BottomSheet
    val showDeleteBottomSheet : Boolean = false,
    val noteToDelete : DatabaseRelation? = null,

    val showDeleteListBottomSheet : Boolean = false,
    val noteToDeleteList : List<DatabaseRelation>? = null,
)
