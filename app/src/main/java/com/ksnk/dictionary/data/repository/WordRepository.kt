package com.ksnk.dictionary.data.repository


import androidx.lifecycle.LiveData
import com.ksnk.dictionary.data.dao.WordDao
import com.ksnk.dictionary.data.entity.Word
import kotlinx.coroutines.flow.Flow


class WordRepository(private val wordDao: WordDao) {
    fun insertWord(word: Word) {
        wordDao.insertWord(word)
    }

    fun deleteWord(word: Word) {
        wordDao.deleteWord(word)
    }

    fun getAllWords(): LiveData<List<Word>> {
        return wordDao.getAllWords()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Word>> {
        return wordDao.searchDatabase(searchQuery)
    }

    fun getAllEngAsc(): LiveData<List<Word>> {
        return wordDao.getAllEngAsc()
    }

    fun getAllUrkAsc(): LiveData<List<Word>> {
        return wordDao.getAllUkrAsc()
    }

    fun getAllEngDesc(): LiveData<List<Word>> {
        return wordDao.getAllEngDesc()
    }

    fun getAllUkrDesc(): LiveData<List<Word>> {
        return wordDao.getAllUrkDesc()
    }

    fun getAllDesc(): LiveData<List<Word>> {
        return wordDao.getAllDesc()
    }

    fun deleteAll() {
        wordDao.deleteAll()
    }
}