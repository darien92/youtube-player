package com.darien.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.darien.data.commons.Commons.DbConstants.words_table_name
import com.darien.data.models.Word

@Entity(tableName = words_table_name)
data class WordsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val word: String,
    val timestamp: Long
) {
    companion object {
        fun fromWordToEntity(word: Word): WordsEntity =
            WordsEntity(id = word.id, word = word.word, timestamp = word.timestamp)
    }

    fun toWordFromEntity(): Word =
        Word(id = this.id, word = this.word, timestamp = this.timestamp)
}
