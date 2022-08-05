package com.darien.domain

import com.darien.core.redux.DomainError
import com.darien.data.models.Word
import com.darien.data.repositories.SearchWordRepository
import com.darien.domain.data.SearchActions
import com.darien.domain.data.SearchViewState
import com.darien.domain.redux.SearchReducer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class SearchReducerTest {
    private lateinit var sut: SearchReducer
    private val repository: SearchWordRepository = mock()
    private val initialState =
        SearchViewState(error = null, isLoading = false, word = "", selectedWord = "")
    private val wordStr = "Hello"
    private val mockedWord = Word(id = 123, word = wordStr)
    private val mockedList: MutableList<Word> = ArrayList()
    private val errorMessage = "Something went wrong"
    private val mockedError = DomainError(errorCode = 123, errorMessage = errorMessage)

    @Before
    fun setUp() {
        mockedList.add(Word(id = 1234, word = "Hello"))
        mockedList.add(Word(id = 1235, word = "Hello2"))
        runBlocking {
            whenever(repository.getWords(wordStr)).thenReturn(mockedList)
            whenever(repository.saveWord(mockedWord)).thenReturn(mockedWord)
        }
        sut = SearchReducer(repository)
    }

    @Test
    fun searchReducerTest_whenStartLoading_shouldSetIsLoadingToTrue(): Unit = runBlocking {
        val expectedState = initialState.copy(isLoading = true)
        val currState = sut.reduce(initialState, SearchActions.StartLoading)
        assertEquals(expectedState.currWords, currState.currWords)
        assertEquals(expectedState.word, currState.word)
        assertEquals(expectedState.selectedWord, currState.selectedWord)
        assertEquals(expectedState.error, currState.error)
        assertTrue(currState.isLoading)
    }

    @Test
    fun searchReducerTest_whenStopLoading_shouldSetIsLoadingToFalse(): Unit = runBlocking {
        val expectedState = initialState.copy(isLoading = false)
        val currState = sut.reduce(initialState, SearchActions.StopLoading)
        assertEquals(expectedState.currWords, currState.currWords)
        assertEquals(expectedState.word, currState.word)
        assertEquals(expectedState.selectedWord, currState.selectedWord)
        assertEquals(expectedState.error, currState.error)
        assertFalse(currState.isLoading)
    }

    @Test
    fun searchReducerTest_whenWordChanges_shouldChangeWordInState(): Unit = runBlocking {
        val expectedState = initialState.copy(word = wordStr)
        val currState = sut.reduce(initialState, SearchActions.WordTyped(word = wordStr))
        assertEquals(mockedList, currState.currWords)
        assertEquals(wordStr, currState.word)
        assertEquals(expectedState.selectedWord, currState.selectedWord)
        assertEquals(expectedState.error, currState.error)
        assertEquals(expectedState.isLoading, currState.isLoading)
    }

    @Test
    fun searchReducerTest_whenWordIsSelected_shouldChangeSelectedWordInState(): Unit = runBlocking {
        val expectedState = initialState.copy(selectedWord = wordStr)
        val currState = sut.reduce(initialState, SearchActions.WordSelected(word = wordStr))
        assertEquals(initialState.currWords, currState.currWords)
        assertEquals(initialState.word, currState.word)
        assertEquals(wordStr, currState.selectedWord)
        assertEquals(expectedState.error, currState.error)
        assertEquals(expectedState.isLoading, currState.isLoading)
    }

    @Test
    fun searchReducerTest_whenNewWord_shouldSetWordAndSelectedWordToNewWord(): Unit = runBlocking {
        val expectedState = initialState.copy(word = wordStr, selectedWord = wordStr)
        val currState =
            sut.reduce(initialState, SearchActions.NewWord(wordId = 123, word = wordStr))
        assertEquals(initialState.currWords, currState.currWords)
        assertEquals(wordStr, currState.word)
        assertEquals(wordStr, currState.selectedWord)
        assertEquals(expectedState.error, currState.error)
        assertEquals(expectedState.isLoading, currState.isLoading)
    }

    @Test
    fun searchReducerTest_whenError_ShouldPropagateTheError(): Unit = runBlocking {
        val expectedState = initialState.copy(error = mockedError, isLoading = false)
        val currState = sut.reduce(initialState, SearchActions.Error(error = mockedError))
        assertEquals(expectedState.isLoading, currState.isLoading)
        assertEquals(expectedState.error?.errorCode, currState.error?.errorCode)
        assertEquals(expectedState.error?.errorMessage, currState.error?.errorMessage)
    }
}