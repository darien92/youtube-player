package com.darien.data.repositories

import com.darien.data.db.WordsDAO
import com.darien.data.db.WordsEntity
import com.darien.data.models.Word
import javax.inject.Inject

class SearchWordRepository @Inject constructor(private val wordsDAO: WordsDAO) {
    suspend fun saveWord(word: Word): Word {
        val wordEntity = WordsEntity.fromWordToEntity(word)
        if (!wordExists(word = word.word)) {
            wordsDAO.addWord(wordEntity)
        }
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

    //prevents adding an already existing word (there's probably a better way)
    private suspend fun wordExists(word: String): Boolean {
        val matchingWords = wordsDAO.getMatchingWords(query = word)
        return matchingWords != null && matchingWords.isNotEmpty()
    }
}