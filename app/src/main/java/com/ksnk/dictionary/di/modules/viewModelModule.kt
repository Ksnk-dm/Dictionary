package com.ksnk.dictionary.di.modules

import com.ksnk.dictionary.ui.listFragment.ListViewModel
import com.ksnk.dictionary.ui.main.MainViewModel
import com.ksnk.dictionary.ui.settingsFragment.SettingsViewModel
import com.ksnk.dictionary.ui.translateFragment.TranslateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainViewModel= module{
    viewModel {
        MainViewModel(get(),get())
    }

    viewModel {
        ListViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(),get())
    }

    viewModel {
        TranslateViewModel(get())
    }
}