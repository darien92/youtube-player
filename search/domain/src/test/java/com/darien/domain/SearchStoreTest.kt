package com.darien.domain

import com.darien.data.models.Word
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

    private fun testInitialStates() {
        assertEquals(initialState.error, sut.state.value.error)
        assertEquals(initialState.isLoading, sut.state.value.isLoading)
        assertEquals(initialState.word, sut.state.value.word)
        assertEquals(initialState.selectedWord, sut.state.value.selectedWord)
        assertEquals(initialState.currWords, sut.state.value.currWords)
    }
}