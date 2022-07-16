package com.ksnk.dictionary.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ksnk.dictionary.data.entity.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWord(word: Word)

    @Delete
    fun deleteWord(word: Word)

    @Query("select * from wordTable")
    fun getAllWords(): LiveData<List<Word>>

    @Query("SELECT * FROM wordTable WHERE wordEng LIKE :searchQuery OR wordTranslate LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<Word>>

    @Query("SELECT * FROM wordTable ORDER BY LOWER(wordEng) ASC ")
    fun getAllEngAsc(): LiveData<List<Word>>

    @Query("SELECT * FROM wordTable ORDER BY LOWER(wordTranslate) ASC ")
    fun getAllUkrAsc(): LiveData<List<Word>>

    @Query("SELECT * FROM wordTable ORDER BY LOWER(wordEng) DESC ")
    fun getAllEngDesc(): LiveData<List<Word>>

    @Query("SELECT * FROM wordTable ORDER BY LOWER(wordTranslate) DESC ")
    fun getAllUrkDesc(): LiveData<List<Word>>

    @Query("SELECT * FROM wordTable ORDER BY wordID DESC ")
    fun getAllDesc(): LiveData<List<Word>>
}