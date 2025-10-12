package org.notes.multi.action

import org.notes.multi.localdata.database.NotesEntity

sealed interface HomeAction {

    //Insert Notes
    data class InsertNotes(val newNotes: NotesEntity): HomeAction

    //Delete BottomSheet
    data class ShowDeleteBottomSheet(
        val showDeleteBottomSheet: Boolean,
        val noteToDelete: NotesEntity?
    ): HomeAction

    //Delete Note
    data object DeleteNote : HomeAction
}