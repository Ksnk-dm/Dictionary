package com.ksnk.dictionary.data.repository

import androidx.lifecycle.LiveData
import com.ksnk.dictionary.data.dao.WordDao
import com.ksnk.dictionary.data.entity.Word

class WordRepository(private val wordDao: WordDao) {
    fun insertWord(word: Word) {
        wordDao.insertWord(word)
    }

    fun getAllWords(): LiveData<List<Word>> {
        return wordDao.getAllWords()
    }
}