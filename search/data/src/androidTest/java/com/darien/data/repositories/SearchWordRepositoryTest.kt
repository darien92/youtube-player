package com.darien.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.darien.data.db.DatabaseService
import com.darien.data.db.WordsDAO
import com.darien.data.models.Word
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class SearchWordRepositoryTest : TestCase() {
    private lateinit var wordsDao: WordsDAO
    private lateinit var db: DatabaseService
    private lateinit var sut: SearchWordRepository

    @Before
    public override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, DatabaseService::class.java
        ).build()
        wordsDao = db.wordsDao()
        sut = SearchWordRepository(wordsDao)
    }


    @Test
    @Throws(Exception::class)
    fun searchWordRepository_whenWordIsAdded_shouldStoreTheWordInDB(): Unit = runBlocking {
        sut.saveWord(Word(id = 123, word = "Hello", timestamp = 123))
        val expectedValue = Word(id = 123, word = "Hello", timestamp = 123)
        val insertedValue = wordsDao.getMatchingWords(query = "Hello")
        assertEquals(expectedValue.id, insertedValue!![0].id)
        assertEquals(expectedValue.word, insertedValue[0].word)
        assertEquals(expectedValue.timestamp, insertedValue[0].timestamp)
    }

    @Test
    @Throws(Exception::class)
    fun searchWordRepository_whenWordStartMatchingWordISRequested_shouldReturnStartMatchingWordsList(): Unit =
        runBlocking {
            createWords()
            val matchingWords1 = sut.getWords("Hel")
            assertEquals(4, matchingWords1.size)
            assertEquals(123, matchingWords1[0].id)
            assertEquals(124, matchingWords1[1].id)
            assertEquals(125, matchingWords1[2].id)
            assertEquals(126, matchingWords1[3].id)
            val matchingWords2 = sut.getWords("Potato")
            assertEquals(1, matchingWords2.size)
            assertEquals(127, matchingWords2[0].id)
            val matchingWords3 = sut.getWords("C")
            assertEquals(1, matchingWords3.size)
            assertEquals(128, matchingWords3[0].id)
        }

    @Test
    @Throws(Exception::class)
    fun searchWordRepository_whenNoMatchingWordIsRequested_shouldReturnEmptyList(): Unit =
        runBlocking {
            createWords()
            val matchingWords = sut.getWords("Dog")
            assertTrue(matchingWords.isEmpty())
        }

    private suspend fun createWords() {
        sut.saveWord(Word(id = 123, word = "Hello", timestamp = 123))
        sut.saveWord(Word(id = 124, word = "Hell", timestamp = 124))
        sut.saveWord(Word(id = 125, word = "Helium", timestamp = 125))
        sut.saveWord(Word(id = 126, word = "Helicopter", timestamp = 126))
        sut.saveWord(Word(id = 127, word = "Potato", timestamp = 127))
        sut.saveWord(Word(id = 128, word = "Car", timestamp = 128))
    }
}