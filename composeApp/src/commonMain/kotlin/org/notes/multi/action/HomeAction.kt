package org.notes.multi.action

import org.notes.multi.localdata.database.NotesEntity
import org.notes.multi.localdata.database.NotesRelation

sealed interface HomeAction {

    //Delete BottomSheet
    data class ShowDeleteBottomSheet(
        val showDeleteBottomSheet: Boolean,
        val noteToDelete: NotesRelation?
    ): HomeAction

    //Delete Note
    data object DeleteNote : HomeAction
}