package com.ksnk.dictionary.ui.translateFragment

import androidx.lifecycle.ViewModel
import com.ksnk.dictionary.data.entity.Word
import com.ksnk.dictionary.data.repository.WordRepository

class TranslateViewModel(private val wordRepository: WordRepository) : ViewModel() {
    fun addWord(userAdd: Word) {
        wordRepository.insertWord(userAdd)
    }
}