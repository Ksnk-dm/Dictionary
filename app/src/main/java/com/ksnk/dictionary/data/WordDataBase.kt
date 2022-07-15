package com.ksnk.dictionary.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ksnk.dictionary.data.dao.WordDao
import com.ksnk.dictionary.data.entity.Word

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordDataBase:RoomDatabase() {
    abstract val wordDao: WordDao
}