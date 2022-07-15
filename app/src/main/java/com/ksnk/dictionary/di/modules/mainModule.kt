package com.ksnk.dictionary.di

import com.ksnk.dictionary.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainViewModel= module{
    viewModel {
        MainViewModel(get())
    }
}