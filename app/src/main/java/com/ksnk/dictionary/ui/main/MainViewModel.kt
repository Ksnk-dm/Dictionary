package com.ksnk.dictionary.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ksnk.dictionary.data.entity.Word
import com.ksnk.dictionary.data.repository.WordRepository

class MainViewModel(private val wordRepository: WordRepository, private val sharedPreferences: SharedPreferences) : ViewModel() {
    fun getAllWords(): LiveData<List<Word>> = wordRepository.getAllWords()

    fun addWord(userAdd: Word) {
        wordRepository.insertWord(userAdd)
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Word>> {
        return wordRepository.searchDatabase(searchQuery)
    }

    fun deleteWord(word: Word){
        wordRepository.deleteWord(word)
    }

    fun getTheme(): Int {
        return sharedPreferences.getInt("theme", 0)
    }

    fun getFragmentId():Int{
        return sharedPreferences.getInt("fragment_id", 0)
    }

    fun setFragmentId(id:Int){
        sharedPreferences.edit().putInt("fragment_id", id).apply()
    }
}