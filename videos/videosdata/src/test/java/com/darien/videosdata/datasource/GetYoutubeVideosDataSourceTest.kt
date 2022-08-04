package com.darien.videosdata.datasource

import com.darien.videosdata.api.YoutubeVideosApi
import com.darien.videosdata.models.data.YoutubeVideosResponseModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class GetYoutubeVideosDataSourceTest{
    private lateinit var sut: GetYoutubeVideosDataSource
    private val runtimeExceptionMessage = "RuntimeException"
    private val query = "hello"
    private val key = "123"
    private val runtimeException: RuntimeException = RuntimeException(runtimeExceptionMessage)
    private val responseMock: YoutubeVideosResponseModel = mock()
    private val api: YoutubeVideosApi = mock()

    @Before
    fun setUp() {
        setupSuccess()
    }
    @Test
    fun `Should call getYoutubeVideos from api once`(): Unit = runBlocking {
        sut.requestVideos(query = query, key = key)
        verify(api, times(1)).getYoutubeVideos(query = query, key = key)
    }

    @Test
    fun `Should return successful result in successful response`(): Unit = runBlocking {
        assertEquals(responseMock, sut.requestVideos(query = query, key = key).first().getOrNull())
    }

    @Test
    fun `Should return exception on network error`(): Unit = runBlocking {
        setupError()
        assertEquals(runtimeExceptionMessage, sut.requestVideos(query = query, key = key).first().exceptionOrNull()!!.message)
    }

    private fun setupSuccess() {
        runBlocking {
            whenever(api.getYoutubeVideos(query = query, key = key)).thenReturn(responseMock)
        }
        sut = GetYoutubeVideosDataSource(api)
    }

    private fun setupError() {
        runBlocking {
            whenever(api.getYoutubeVideos(query = query, key = key)).thenThrow(runtimeException)
        }
        sut = GetYoutubeVideosDataSource(api)
    }
}