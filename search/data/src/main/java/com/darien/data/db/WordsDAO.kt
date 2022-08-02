package com.darien.data.db

import androidx.room.*

@Dao
interface WordsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE) //this resolves the update
    suspend fun addWord(wordsEntity: WordsEntity)

    @Query("SELECT * FROM words WHERE word LIKE :query")
    suspend fun getMatchingWords(query: String): List<WordsEntity>?
}