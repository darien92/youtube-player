package com.darien.ui

import com.darien.core.redux.DomainError
import com.darien.data.models.Word
import com.darien.domain.data.SearchActions
import com.darien.domain.redux.SearchReducer
import com.darien.domain.redux.SearchStore
import com.darien.domain.data.SearchViewState
import com.darien.ui.viewmodels.SearchViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class SearchViewModelTest {

    private lateinit var sut: SearchViewModel
    private lateinit var store: SearchStore
    private val reducer: SearchReducer = mock()
    private val initialState = SearchViewState(error = null, isLoading = false, word = "", selectedWord = "", currWords = ArrayList())
    private val words: MutableList<Word> = ArrayList()
    private val hello = "Hello"
    private val errorMessage = "Something went wrong"
    private val mockedError = DomainError(errorCode = 123, errorMessage = errorMessage)

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
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
        store = SearchStore(reducer)
        sut = SearchViewModel(store, dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun searchViewModel_whenWordTyped_shouldSetWordToTypedWordAndCurrWordsToWordsList(): Unit = runTest {
        sut.onWordTyped(word = hello)
        delay(100)
        assertEquals(hello, sut.uiState.value.word)
        assertEquals(words, sut.uiState.value.currWords)
    }

    @Test
    fun searchViewModel_whenWordSelected_shouldSelectWord(): Unit = runTest {
        sut.onSelectedWord(word = hello)
        delay(100)
        assertEquals(hello, sut.uiState.value.selectedWord)
    }

    @Test
    fun searchViewModel_whenSearch_wordShouldBeEqualsToSelectedWord(): Unit = runTest {
        sut.onNewWord(wordId = 123, word = hello)
        delay(1000)
        assertEquals(hello, sut.uiState.value.selectedWord)
    }
}