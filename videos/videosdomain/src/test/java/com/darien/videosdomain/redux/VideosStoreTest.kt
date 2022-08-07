package com.darien.videosdomain.redux

import com.darien.core.redux.DomainError
import com.darien.videosdata.models.domain.YoutubeVideoDomainModel
import com.darien.videosdomain.data.VideosActions
import com.darien.videosdomain.data.VideosViewState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


internal class VideosStoreTest {
    private lateinit var sut: VideosStore
    private val reducer: VideosReducer = mock()
    private val initialState: VideosViewState =
        VideosViewState(error = null, isLoading = false, isFirstLoading = false)
    private val query = "Hello"
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

    @Before
    fun setUp() {
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
                initialState.copy(isFirstLoading = false)
            )
        }
        setupSuccess()
    }

    @Test
    fun `Should set is first loading to true on start first loading action`(): Unit = runBlocking {
        testInitialStates()
        sut.dispatch(VideosActions.StartLoadingFirstTime)
        assertTrue(sut.state.value.isFirstLoading)
    }

    @Test
    fun `Should set is first loading to false on stop first loading action`(): Unit = runBlocking {
        testInitialStates()
        sut.dispatch(VideosActions.StopLoadingFirstTime)
        assertFalse(sut.state.value.isFirstLoading)
    }

    @Test
    fun `Should set is loading to true on start loading action`(): Unit = runBlocking {
        testInitialStates()
        sut.dispatch(VideosActions.StartLoading)
        assertTrue(sut.state.value.isLoading)
    }

    @Test
    fun `Should set is loading to false on stop loading action`(): Unit = runBlocking {
        testInitialStates()
        sut.dispatch(VideosActions.StopLoading)
        assertFalse(sut.state.value.isLoading)
    }

    @Test
    fun `Should set videos in successful response`(): Unit = runBlocking {
        testInitialStates()
        sut.dispatch(VideosActions.LoadVideos(query, key))
        assertFalse(sut.state.value.isLoading)
        assertEquals(youtubeVideosResponse, sut.state.value.videos)
        assertNull(sut.state.value.error)
    }

    @Test
    fun `Should set network error in network error`(): Unit = runBlocking {
        testInitialStates()
        setupNetworkError()
        sut.dispatch(VideosActions.LoadVideos(query, key))
        assertEquals(networkError, sut.state.value.error)
        assertEquals(initialState.videos, sut.state.value.videos)
    }

    @Test
    fun `Should set request error in request error`(): Unit = runBlocking {
        testInitialStates()
        setupSuccessError()
        sut.dispatch(VideosActions.LoadVideos(query, key))
        assertEquals(requestError, sut.state.value.error)
        assertEquals(initialState.videos, sut.state.value.videos)
    }

    private fun setupSuccess() {
        runBlocking {
            whenever(reducer.reduce(initialState, VideosActions.LoadVideos(query, key))).thenReturn(
                initialState.copy(
                    error = null,
                    isLoading = false,
                    isFirstLoading = false,
                    videos = youtubeVideosResponse
                )
            )
        }
        sut = VideosStore(reducer = reducer)
    }

    private fun setupNetworkError() {
        runBlocking {
            whenever(reducer.reduce(initialState, VideosActions.LoadVideos(query, key))).thenReturn(
                initialState.copy(
                    error = networkError,
                    isLoading = false,
                    isFirstLoading = false
                )
            )
        }
        sut = VideosStore(reducer = reducer)
    }

    private fun setupSuccessError() {
        runBlocking {
            whenever(reducer.reduce(initialState, VideosActions.LoadVideos(query, key))).thenReturn(
                initialState.copy(
                    error = requestError,
                    isLoading = false,
                    isFirstLoading = false
                )
            )
        }
        sut = VideosStore(reducer = reducer)
    }

    private fun testInitialStates() {
        assertEquals(initialState.error, sut.state.value.error)
        assertEquals(initialState.isLoading, sut.state.value.isLoading)
        assertEquals(initialState.isFirstLoading, sut.state.value.isFirstLoading)
        assertEquals(initialState.videos, sut.state.value.videos)
    }
}