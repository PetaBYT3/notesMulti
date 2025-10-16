package org.notes.multi.module

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.notes.multi.getNotesDatabase
import org.notes.multi.localdata.database.NotesDatabase
import org.notes.multi.repository.NotesRepository
import org.notes.multi.viewmodel.HomeViewModel
import org.notes.multi.viewmodel.NoteViewModel

object AppModule {

    private val databaseModule = module {
        single { getNotesDatabase() }
        single { get<NotesDatabase>().notesDao() }
    }

    private val repositoryModule = module {
        singleOf(::NotesRepository)
    }

    private val viewModelModule = module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::NoteViewModel)
    }

    fun getAll() = listOf(
        databaseModule,
        viewModelModule,
        repositoryModule,
    )
}