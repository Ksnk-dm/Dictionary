package com.ksnk.dictionary.di

import android.app.Application
import com.ksnk.dictionary.di.modules.mainViewModel
import com.ksnk.dictionary.di.modules.preferencesModule
import com.ksnk.dictionary.di.modules.repositoryModule
import com.ksnk.dictionary.di.modules.userDB
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                userDB, mainViewModel, repositoryModule, preferencesModule
            )
        }
    }
}