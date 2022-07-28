package com.darien.data.db

import androidx.room.*

@Dao
interface WordsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE) //this resolves the update
    suspend fun addWord(noteEntity: WordsEntity)

    @Query("SELECT * FROM words WHERE id = :id")
    suspend fun getWord(id:Long): WordsEntity?

    @Query("SELECT * FROM words WHERE word LIKE :query")
    suspend fun getMatchingWords(query: String): List<WordsEntity>?

    @Delete
    suspend fun deleteNoteEntity(noteEntity: WordsEntity)
}