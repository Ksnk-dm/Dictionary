package com.ksnk.dictionary.ui.listFragment

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ksnk.dictionary.FilterValues
import com.ksnk.dictionary.data.entity.Word
import com.ksnk.dictionary.data.repository.WordRepository

class ListViewModel(private val wordRepository: WordRepository, private val sharedPreferences: SharedPreferences) : ViewModel() {
    fun getAllWords(): LiveData<List<Word>> = wordRepository.getAllWords()

    fun addWord(userAdd: Word) {
        wordRepository.insertWord(userAdd)
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Word>> {
        return wordRepository.searchDatabase(searchQuery)
    }

    fun deleteWord(word: Word) {
        wordRepository.deleteWord(word)
    }

    fun getAllEngAsc(): LiveData<List<Word>> = wordRepository.getAllEngAsc()

    fun getAllUkrAsc(): LiveData<List<Word>> = wordRepository.getAllUrkAsc()

    fun getAllEngDesc(): LiveData<List<Word>> = wordRepository.getAllEngDesc()

    fun getAllUkrDesc(): LiveData<List<Word>> = wordRepository.getAllUkrDesc()

    fun getAllDesc(): LiveData<List<Word>> = wordRepository.getAllDesc()

    fun setFilterValue(value: Int){
        sharedPreferences.edit().putInt("filter", value).apply()
    }

    fun getFilterValue():Int{
        return sharedPreferences.getInt("filter", 0)
    }
}