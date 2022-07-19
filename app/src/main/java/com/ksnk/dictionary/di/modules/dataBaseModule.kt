package com.ksnk.dictionary.di.modules

import android.app.Application
import androidx.room.Room
import com.ksnk.dictionary.data.dao.WordDao
import com.ksnk.dictionary.data.WordDataBase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val userDB = module {
    fun provideDataBase(application: Application): WordDataBase {
        return Room.databaseBuilder(application, WordDataBase::class.java, "WORDDB")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDao(dataBase: WordDataBase): WordDao {
        return dataBase.wordDao
    }
    single { provideDataBase(androidApplication()) }
    single { provideDao(get()) }

}
