package org.notes.multi.module

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.notes.multi.getNotesDatabase
import org.notes.multi.localdata.database.NotesDatabase
import org.notes.multi.repository.NotesRepository
import org.notes.multi.viewmodel.HomeViewModel

object AppModule {

    val databaseModule = module {
        single { getNotesDatabase() }
        single { get<NotesDatabase>().notesDao() }
    }

    val repositoryModule = module {
        singleOf(::NotesRepository)
    }

    val viewModelModule = module {
        viewModelOf(::HomeViewModel)
    }

    fun getAll() = listOf(
        databaseModule,
        viewModelModule,
        repositoryModule,
    )
}