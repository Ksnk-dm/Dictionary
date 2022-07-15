package com.ksnk.dictionary.ui.listFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ksnk.dictionary.data.entity.Word
import com.ksnk.dictionary.data.repository.WordRepository

class ListViewModel(private val wordRepository: WordRepository): ViewModel() {
    fun getAllWords(): LiveData<List<Word>> = wordRepository.getAllWords()

    fun addWord(userAdd: Word) {
        wordRepository.insertWord(userAdd)
    }
}