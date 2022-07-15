package com.ksnk.dictionary.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ksnk.dictionary.data.entity.Word
import com.ksnk.dictionary.data.repository.WordRepository

class MainViewModel(private val wordRepository: WordRepository) : ViewModel() {
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

}