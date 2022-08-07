package com.darien.videosdomain.redux

import com.darien.core.network.NetworkResponseErrorTypes
import com.darien.core.redux.DomainError
import com.darien.videosdata.models.domain.ResponseOrError
import com.darien.videosdata.models.domain.YoutubeVideoDomainModel
import com.darien.videosdata.repository.GetYoutubeVideosRepository
import com.darien.videosdomain.data.VideosActions
import com.darien.videosdomain.data.VideosViewState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class VideosReducerTest {
    private lateinit var sut: VideosReducer
    private lateinit var successfulResponse: ResponseOrError
    private val repository: GetYoutubeVideosRepository = mock()
    private val initialState: VideosViewState =
        VideosViewState(error = null, isLoading = false, isFirstLoading = true)
    private val query = "Hello"
    private val key = "123"
    private val networkErrorResponse =
        ResponseOrError(error = NetworkResponseErrorTypes.NETWORK_ERROR)
    private val requestErrorResponse =
        ResponseOrError(error = NetworkResponseErrorTypes.REQUEST_ERROR)
    private val youtubeVideoDomainModel = YoutubeVideoDomainModel(
        id = "123",
        publishedAt = "123",
        title = "title",
        description = "description",
        thumbnail = "thumb",
        channelName = "channel"
    )
    private val youtubeVideosResponse: ArrayList<YoutubeVideoDomainModel> = ArrayList()

    @Before
    fun setUp() {
        youtubeVideosResponse.add(youtubeVideoDomainModel)
        successfulResponse = ResponseOrError(response = youtubeVideosResponse)
        setupSuccess()
    }

    @Test
    fun `Should setFirstLoading to true in StartirstLoading Action`(): Unit = runBlocking {
        val expectedState = initialState.copy(isFirstLoading = true)
        val currState = sut.reduce(initialState, VideosActions.StartLoadingFirstTime)
        TestCase.assertEquals(expectedState.videos, currState.videos)
        TestCase.assertEquals(expectedState.error, currState.error)
        TestCase.assertTrue(currState.isFirstLoading)
    }

    @Test
    fun `Should setFirstLoading to false in StopFirstLoading Action`(): Unit = runBlocking {
        val expectedState = initialState.copy(isFirstLoading = false)
        val currState = sut.reduce(initialState, VideosActions.StopLoadingFirstTime)
        TestCase.assertEquals(expectedState.videos, currState.videos)
        TestCase.assertEquals(expectedState.error, currState.error)
        TestCase.assertFalse(currState.isFirstLoading)
    }

    @Test
    fun `Should setLoading to true in StartLoading Action`(): Unit = runBlocking {
        val expectedState = initialState.copy(isLoading = true)
        val currState = sut.reduce(initialState, VideosActions.StartLoading)
        TestCase.assertEquals(expectedState.videos, currState.videos)
        TestCase.assertEquals(expectedState.error, currState.error)
        TestCase.assertTrue(currState.isLoading)
    }

    @Test
    fun `Should setLoading to false in StopLoading Action`(): Unit = runBlocking {
        val expectedState = initialState.copy(isLoading = false)
        val currState = sut.reduce(initialState, VideosActions.StopLoading)
        TestCase.assertEquals(expectedState.videos, currState.videos)
        TestCase.assertEquals(expectedState.error, currState.error)
        TestCase.assertFalse(currState.isLoading)
    }

    @Test
    fun `Should set Loading to false and get videos list in successful LoadVideos Action`(): Unit =
        runBlocking {
            val expectedState = initialState.copy(isLoading = false, videos = youtubeVideosResponse)
            val currState = sut.reduce(initialState, VideosActions.LoadVideos(query, key))
            TestCase.assertEquals(expectedState.videos, currState.videos)
            TestCase.assertEquals(expectedState.error, currState.error)
            TestCase.assertFalse(currState.isLoading)
        }

    @Test
    fun `Should set Loading to false and get networkError in NetworkError LoadVideos Action`(): Unit =
        runBlocking {
            setupNetworkError()
            val expectedState = initialState.copy(
                isLoading = false, error = DomainError(
                    errorCode = 0,
                    errorMessage = "Network error"
                )
            )
            val currState = sut.reduce(initialState, VideosActions.LoadVideos(query, key))
            TestCase.assertEquals(expectedState.videos, currState.videos)
            TestCase.assertEquals(expectedState.error, currState.error)
            TestCase.assertFalse(currState.isLoading)
        }

    @Test
    fun `Should set Loading to false and get requestError in RequestError LoadVideos Action`(): Unit =
        runBlocking {
            setupRequestError()
            val expectedState = initialState.copy(
                isLoading = false, error = DomainError(
                    errorCode = 1,
                    errorMessage = "Request error"
                )
            )
            val currState = sut.reduce(initialState, VideosActions.LoadVideos(query, key))
            TestCase.assertEquals(expectedState.videos, currState.videos)
            TestCase.assertEquals(expectedState.error, currState.error)
            TestCase.assertFalse(currState.isLoading)
        }

    private fun setupSuccess() {
        runBlocking {
            whenever(repository.getVideos(query, key)).thenReturn(
                Result.success(successfulResponse)
            )
        }
        sut = VideosReducer(repository)
    }

    private fun setupNetworkError() {
        runBlocking {
            whenever(repository.getVideos(query, key)).thenReturn(
                Result.success(networkErrorResponse)
            )
        }
        sut = VideosReducer(repository)
    }

    private fun setupRequestError() {
        runBlocking {
            whenever(repository.getVideos(query, key)).thenReturn(
                Result.success(requestErrorResponse)
            )
        }
        sut = VideosReducer(repository)
    }
}