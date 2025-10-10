package org.notes.multi

import org.notes.multi.localdata.database.NotesDatabase

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

//Room Database
expect fun getNotesDatabase(): NotesDatabase