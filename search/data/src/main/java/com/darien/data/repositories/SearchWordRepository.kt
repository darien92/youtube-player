package com.darien.data.repositories

import com.darien.data.db.WordsDAO
import com.darien.data.db.WordsEntity
import com.darien.data.models.Word
import javax.inject.Inject

class SearchWordRepository @Inject constructor(private val wordsDAO: WordsDAO) {
    suspend fun saveWord(word: Word): Word {
        val wordEntity = WordsEntity.fromWordToEntity(word)
        wordsDAO.addWord(wordEntity)
        return word
    }

    suspend fun getWords(query: String): List<Word> {
        val searchQuery =
            "${query}%" //adds % to search words that matches with query in the beginning of the word
        val matchingWords = wordsDAO.getMatchingWords(query = searchQuery)
        val ans: MutableList<Word> = ArrayList()
        if (matchingWords != null) {
            for (matchingWord in matchingWords) {
                ans.add(matchingWord.toWordFromEntity())
            }
        }
        return ans
    }
}