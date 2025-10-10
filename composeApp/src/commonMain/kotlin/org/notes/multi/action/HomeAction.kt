package org.notes.multi.action

import org.notes.multi.localdata.database.NotesEntity

sealed interface HomeAction {

    data class InsertNotes(val newNotes: NotesEntity): HomeAction
}