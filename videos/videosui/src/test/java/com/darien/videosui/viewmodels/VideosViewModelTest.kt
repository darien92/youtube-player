package com.darien.videosui.viewmodels

import com.darien.core.redux.DomainError
import com.darien.videosdata.models.domain.YoutubeVideoDomainModel
import com.darien.videosdomain.data.VideosActions
import com.darien.videosdomain.data.VideosViewState
import com.darien.videosdomain.redux.VideosReducer
import com.darien.videosdomain.redux.VideosStore
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class VideosViewModelTest {
    private lateinit var sut: VideosViewModel
    private lateinit var store: VideosStore
    private val reducer: VideosReducer = mock()
    private val initialState =
        VideosViewState(error = null, isLoading = false, isFirstLoading = true)
    private val query = "hello"
    private val key = "123"
    private val youtubeVideoDomainModel = YoutubeVideoDomainModel(
        id = "123",
        publishedAt = "123",
        title = "title",
        description = "description",
        thumbnail = "thumb",
        channelName = "channel"
    )
    private val youtubeVideosResponse: ArrayList<YoutubeVideoDomainModel> = ArrayList()
    private val networkError = DomainError(
        errorCode = 0,
        errorMessage = "Network error"
    )
    private val requestError = DomainError(
        errorCode = 1,
        errorMessage = "Request error"
    )
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        youtubeVideosResponse.add(youtubeVideoDomainModel)
        runBlocking {
            whenever(reducer.reduce(initialState, VideosActions.StartLoadingFirstTime)).thenReturn(
                initialState.copy(isFirstLoading = true)
            )
            whenever(reducer.reduce(initialState, VideosActions.StopLoadingFirstTime)).thenReturn(
                initialState.copy(isFirstLoading = false)
            )
            whenever(reducer.reduce(initialState, VideosActions.StartLoading)).thenReturn(
                initialState.copy(isLoading = true)
            )
            whenever(reducer.reduce(initialState, VideosActions.StopLoading)).thenReturn(
                initialState.copy(isLoading = false)
            )
        }
        setupSuccess()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Should Set Loading to true on Start Loading action`(): Unit = runTest{
        sut.startLoading()
        delay(100)
        assertTrue(sut.uiState.value.isLoading)
    }

    @Test
    fun `Should Set Loading to false on Stop Loading action`(): Unit = runTest{
        sut.stopLoading()
        delay(100)
        assertFalse(sut.uiState.value.isLoading)
    }

    @Test
    fun `Should Set Loading FirstTime to true on Start Loading Initially action`(): Unit = runTest{
        sut.startLoadingInitially()
        delay(100)
        assertTrue(sut.uiState.value.isFirstLoading)
    }

    @Test
    fun `Should Set Loading FirstTime to false on Stop Loading Initially action`(): Unit = runTest{
        sut.stopLoadingInitially()
        delay(100)
        assertFalse(sut.uiState.value.isFirstLoading)
    }

    @Test
    fun `Should update videos on successful response`(): Unit = runTest {
        sut.searchVideos(query, key)
        delay(100)
        assertFalse(sut.uiState.value.isLoading)
        assertEquals(youtubeVideosResponse, sut.uiState.value.videos)
        assertNull(sut.uiState.value.error)
    }

    @Test
    fun `Should Get network error on network error`(): Unit = runTest {
        setupNetworkError()
        sut.searchVideos(query, key)
        delay(100)
        assertFalse(sut.uiState.value.isLoading)
        assertEquals(initialState.videos, sut.uiState.value.videos)
        assertEquals(networkError, sut.uiState.value.error)
    }

    @Test
    fun `Should Get request error on request error`(): Unit = runTest {
        setupRequestError()
        sut.searchVideos(query, key)
        delay(100)
        assertFalse(sut.uiState.value.isLoading)
        assertEquals(initialState.videos, sut.uiState.value.videos)
        assertEquals(requestError, sut.uiState.value.error)
    }

    private fun setupSuccess() {
        runBlocking {
            whenever(reducer.reduce(initialState, VideosActions.LoadVideos(query, key))).thenReturn(
                initialState.copy(error = null, isLoading = false, videos = youtubeVideosResponse)
            )
        }
        store = VideosStore(reducer)
        sut = VideosViewModel(store, dispatcher)
    }

    private fun setupNetworkError() {
        runBlocking {
            whenever(reducer.reduce(initialState, VideosActions.LoadVideos(query, key))).thenReturn(
                initialState.copy(error = networkError, isLoading = false)
            )

        }
        store = VideosStore(reducer)
        sut = VideosViewModel(store, dispatcher)
    }

    private fun setupRequestError() {
        runBlocking {
            whenever(reducer.reduce(initialState, VideosActions.LoadVideos(query, key))).thenReturn(
                initialState.copy(error = requestError, isLoading = false)
            )
        }
        store = VideosStore(reducer)
        sut = VideosViewModel(store, dispatcher)
    }
}