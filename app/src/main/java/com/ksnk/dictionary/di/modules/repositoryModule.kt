package com.ksnk.dictionary.di.modules

import com.ksnk.dictionary.data.repository.WordRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { WordRepository(get()) }
}