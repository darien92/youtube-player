package com.darien.videosui.viewmodels

import com.darien.core.redux.DomainError
import com.darien.videosdata.models.domain.YoutubeVideoDomainModel
import com.darien.videosdomain.data.VideosActions
import com.darien.videosdomain.data.VideosViewState
import com.darien.videosdomain.redux.VideosReducer
import com.darien.videosdomain.redux.VideosStore
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
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
        VideosViewState(error = null, isLoading = false, isFirstLoading = false)
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
    fun `Should update videos on successful response`(): Unit = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            sut.loadVideosInitially(query, key)
            delay(100)
            assertFalse(sut.uiState.value.isLoading)
            assertEquals(youtubeVideosResponse, sut.uiState.value.videos)
            assertNull(sut.uiState.value.error)
        }
        job.cancel()
    }

    @Test
    fun `Should update videos on successful response pagination`(): Unit = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            sut.loadVideos(query, key)
            delay(100)
            assertFalse(sut.uiState.value.isLoading)
            assertEquals(youtubeVideosResponse, sut.uiState.value.videos)
            assertNull(sut.uiState.value.error)
        }
        job.cancel()
    }

    @Test
    fun `Should Get network error on network error`(): Unit = runTest {
        setupNetworkError()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            sut.loadVideosInitially(query, key)
            delay(1000)
            assertFalse(sut.uiState.value.isLoading)
            assertEquals(initialState.videos, sut.uiState.value.videos)
            assertEquals(networkError, sut.uiState.value.error)
        }
        job.cancel()
    }

    @Test
    fun `Should Get request error on request error`(): Unit = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            setupRequestError()
            sut.loadVideosInitially(query, key)
            delay(100)
            assertFalse(sut.uiState.value.isLoading)
            assertEquals(initialState.videos, sut.uiState.value.videos)
            assertEquals(requestError, sut.uiState.value.error)
        }
        job.cancel()
    }

    @Test
    fun `Should Get network error on network error pagination`(): Unit = runTest {
        setupNetworkError()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            sut.loadVideos(query, key)
            delay(1000)
            assertFalse(sut.uiState.value.isLoading)
            assertEquals(initialState.videos, sut.uiState.value.videos)
            assertEquals(networkError, sut.uiState.value.error)
        }
        job.cancel()
    }

    @Test
    fun `Should Get request error on request error pagination`(): Unit = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            setupRequestError()
            sut.loadVideos(query, key)
            delay(100)
            assertFalse(sut.uiState.value.isLoading)
            assertEquals(initialState.videos, sut.uiState.value.videos)
            assertEquals(requestError, sut.uiState.value.error)
        }
        job.cancel()
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