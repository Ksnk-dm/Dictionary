package com.ksnk.dictionary.ui.settingsFragment

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ksnk.dictionary.data.entity.Word
import com.ksnk.dictionary.data.repository.WordRepository
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

class SettingsViewModel(
    private val wordRepository: WordRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    fun setSetTheme(theme: Int) {
        sharedPreferences.edit().putInt("theme", theme).apply()
    }

    fun getTheme(): Int {
        return sharedPreferences.getInt("theme", 0)
    }

    fun setNameProfile(name: String) {
        sharedPreferences.edit().putString("name", name).apply()
    }

    fun getNameProfile(): String? {
        return sharedPreferences.getString("name", "Profile")
    }

    fun deleteAllWords() {
        wordRepository.deleteAll()
    }

    fun getAll(): LiveData<List<Word>> {
       return wordRepository.getAllEngAsc()
    }
}