package org.notes.multi.module

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.notes.multi.repository.TestRepository
import org.notes.multi.viewmodel.HomeViewModel

object AppModule {

    val repositoryModule = module {
        singleOf(::TestRepository)
    }

    val viewModelModule = module {
        viewModelOf(::HomeViewModel)
    }

    fun getAll() = listOf(
        viewModelModule,
        repositoryModule
    )
}