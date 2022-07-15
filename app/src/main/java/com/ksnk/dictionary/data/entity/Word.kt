package com.ksnk.dictionary.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wordTable")
data class Word(
    @PrimaryKey(autoGenerate = true)
    var wordID: Int = 0,
    @ColumnInfo(name = "wordEng")
    val wordEng: String,
    @ColumnInfo(name = "wordTranslate")
    val wordTranslate:String
)
