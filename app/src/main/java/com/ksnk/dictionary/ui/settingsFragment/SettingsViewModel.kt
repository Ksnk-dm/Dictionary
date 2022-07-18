package com.ksnk.dictionary.ui.settingsFragment

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

class SettingsViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    fun setSetTest(text: String) {
        sharedPreferences.edit().putString("test", "test").apply()
    }

    fun getTestText(): String? {
        return sharedPreferences.getString("test", "")
    }
}