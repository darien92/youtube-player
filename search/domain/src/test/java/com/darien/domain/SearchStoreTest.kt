package com.darien.domain

import com.darien.core.redux.DomainError
import com.darien.data.models.Word
import com.darien.domain.data.SearchActions
import com.darien.domain.redux.SearchStore
import com.darien.domain.data.SearchViewState
import com.darien.domain.redux.SearchReducer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class SearchStoreTest{
    private lateinit var sut: SearchStore
    private val reducer: SearchReducer = mock()
    private val initialState = SearchViewState(error = null, isLoading = false, word = "", selectedWord = "", currWords = ArrayList())
    private val words: MutableList<Word> = ArrayList()
    private val hello = "Hello"
    private val errorMessage = "Something went wrong"
    private val mockedError = DomainError(errorCode = 123, errorMessage = errorMessage)

    @Before
    fun setUp() {
        words.add(Word(id = 123, "Hello"))
        words.add(Word(id = 124, "Hello2"))
        runBlocking {
            whenever(reducer.reduce(initialState, SearchActions.StartLoading)).thenReturn(initialState.copy(isLoading = true))
            whenever(reducer.reduce(initialState, SearchActions.StopLoading)).thenReturn(initialState.copy(isLoading = false))
            whenever(reducer.reduce(initialState, SearchActions.WordTyped(word = hello))).thenReturn(initialState.copy(currWords = words, word = hello))
            whenever(reducer.reduce(initialState, SearchActions.WordSelected(word = hello))).thenReturn(initialState.copy(selectedWord = hello))
            whenever(reducer.reduce(initialState, SearchActions.NewWord(wordId = 123, word = hello))).thenReturn(initialState.copy(selectedWord = hello, word = hello))
            whenever(reducer.reduce(initialState, SearchActions.Error(error = mockedError))).thenReturn(initialState.copy(error = mockedError))
        }
        sut = SearchStore(reducer = reducer)
    }

    @Test
    fun searchStore_whenIsLoading_shouldSetIsLoadingToTrue(): Unit = runBlocking {
        testInitialStates()
        sut.dispatch(SearchActions.StartLoading)
        assertEquals(true, sut.state.value.isLoading)
    }

    @Test
    fun searchStore_whenStopLoading_shouldSetIsLoadingToFalse(): Unit = runBlocking {
        testInitialStates()
        sut.dispatch(SearchActions.StopLoading)
        assertEquals(false, sut.state.value.isLoading)
    }

    @Test
    fun searchStore_whenWordTyped_shouldSetWordToTypedWordAndReceiveListOfWords(): Unit = runBlocking {
        testInitialStates()
        sut.dispatch(SearchActions.WordTyped(word = hello))
        assertEquals(words, sut.state.value.currWords)
        assertEquals(hello, sut.state.value.word)
    }

    @Test
    fun searchStore_whenWordSelected_shouldSetSelectedWord(): Unit = runBlocking {
        testInitialStates()
        sut.dispatch(SearchActions.WordSelected(word = hello))
        assertEquals(hello, sut.state.value.selectedWord)
    }

    @Test
    fun searchStore_whenNewWord_shouldSetSelectedWordAndWord(): Unit = runBlocking {
        testInitialStates()
        sut.dispatch(SearchActions.NewWord(wordId = 123, word = hello))
        assertEquals(hello, sut.state.value.word)
        assertEquals(hello, sut.state.value.selectedWord)
    }

    @Test
    fun searchStore_whenError_shouldPropagateError(): Unit = runBlocking {
        testInitialStates()
        sut.dispatch(SearchActions.Error(error = mockedError))
        assertEquals(mockedError, sut.state.value.error)
    }

    private fun testInitialStates() {
        assertEquals(initialState.error, sut.state.value.error)
        assertEquals(initialState.isLoading, sut.state.value.isLoading)
        assertEquals(initialState.word, sut.state.value.word)
        assertEquals(initialState.selectedWord, sut.state.value.selectedWord)
        assertEquals(initialState.currWords, sut.state.value.currWords)
    }
}