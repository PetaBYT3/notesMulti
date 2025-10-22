package org.notes.multi.action

import org.notes.multi.localdata.database.DatabaseRelation

sealed interface HomeAction {

    data class IsSelectionEnabled(val isEnabled: Boolean) : HomeAction

    data class SelectNote(val uId : Long, val isSelect: Boolean) : HomeAction

    //Delete BottomSheet
    data class ShowDeleteBottomSheet(
        val showDeleteBottomSheet: Boolean,
        val noteToDelete: DatabaseRelation?
    ): HomeAction

    //Delete Note
    data object DeleteNote : HomeAction

    data object ShowDeleteListBottomSheet : HomeAction
    data object DeleteListNote : HomeAction
}