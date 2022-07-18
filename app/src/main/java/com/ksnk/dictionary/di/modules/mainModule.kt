package com.ksnk.dictionary.di.modules

import com.ksnk.dictionary.ui.listFragment.ListViewModel
import com.ksnk.dictionary.ui.main.MainViewModel
import com.ksnk.dictionary.ui.settingsFragment.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainViewModel= module{
    viewModel {
        MainViewModel(get())
    }

    viewModel {
        ListViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get())
    }
}