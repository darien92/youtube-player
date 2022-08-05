package com.darien.videosdata.repository

import com.darien.core.network.NetworkResponseErrorTypes
import com.darien.videosdata.datasource.GetYoutubeVideosDataSource
import com.darien.videosdata.models.data.*
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.lang.RuntimeException

internal class GetYoutubeVideosRepositoryTest {
    private lateinit var sut: GetYoutubeVideosRepository
    private lateinit var dsSuccessfulResponse: YoutubeVideosResponseModel
    private val query = "hello"
    private val key = "123"
    private val dataSource: GetYoutubeVideosDataSource = mock()
    private val emptyListResponse = YoutubeVideosResponseModel(
        etag = "123",
        items = null,
        kind = "video",
        nextPageToken = "123",
        pageInfo = null,
        regionCode = "123"
    )

    @Before
    fun setUp() {
        setupSuccess()
        val items: MutableList<Item> = ArrayList()
        items.add(
            index = 0,
            element = Item(
                etag = "123",
                id = Id(kind = "video", "123"),
                kind = "Video",
                snippet = Snippet(
                    channelId = "channel",
                    channelTitle = "channel",
                    description = "description",
                    liveBroadcastContent = "liveContent",
                    publishedAt = "123123",
                    publishTime = "1231231",
                    thumbnails = Thumbnails(
                        default = null,
                        medium = Medium(height = 40, url = "url", width = 40),
                        high = null
                    ),
                    title = "title"
                )
            )
        )
        dsSuccessfulResponse = YoutubeVideosResponseModel(
            etag = "123",
            items = items,
            kind = "video",
            nextPageToken = "123",
            pageInfo = null,
            regionCode = "123"
        )
    }

    @Test
    fun getYoutubeVideosRepository_whenGetVideos_shouldCallDSGetVideosOnce(): Unit = runBlocking {
        sut.getVideos(query, key)
        verify(dataSource, times(1)).requestVideos(query, key)
    }

    @Test
    fun getYoutubeVideosRepository_whenSuccess_shouldReturnSuccessfulResponse(): Unit =
        runBlocking {
            val response = sut.getVideos(query, key)
            assertEquals(dsSuccessfulResponse.items?.get(0)?.id?.videoId.toString(), response.first().getOrNull()?.response?.first()?.id)
            assertEquals(dsSuccessfulResponse.items?.get(0)?.snippet?.publishedAt.toString(), response.first().getOrNull()?.response?.first()?.publishedAt)
            assertEquals(dsSuccessfulResponse.items?.get(0)?.snippet?.title.toString(), response.first().getOrNull()?.response?.first()?.title)
            assertEquals(dsSuccessfulResponse.items?.get(0)?.snippet?.description.toString(), response.first().getOrNull()?.response?.first()?.description)
            assertEquals(dsSuccessfulResponse.items?.get(0)?.snippet?.thumbnails?.medium?.url.toString(), response.first().getOrNull()?.response?.first()?.thumbnail)
            assertEquals(dsSuccessfulResponse.items?.get(0)?.snippet?.channelTitle.toString(), response.first().getOrNull()?.response?.first()?.channelName)
            assertNull(response.first().getOrNull()?.error)
        }

    @Test
    fun getYoutubeVideosRepository_whenSuccessWithEmptyList_shouldReturnRequestError(): Unit =
        runBlocking {
            setupEmptyList()
            val response = sut.getVideos(query, key)
            assertNull(response.first().getOrNull()?.response)
            assertEquals(NetworkResponseErrorTypes.REQUEST_ERROR, response.first().getOrNull()?.error)
        }

    @Test
    fun getYoutubeVideosRepository_whenIOException_shouldReturnNetworkErrorResponse(): Unit =
        runBlocking {
            setupNetworkError()
            val response = sut.getVideos(query, key)
            assertNull(response.first().getOrNull()?.response)
            assertEquals(NetworkResponseErrorTypes.NETWORK_ERROR, response.first().getOrNull()?.error)
        }

    @Test
    fun getYoutubeVideosRepository_whenRuntimeException_shouldReturnRequestErrorResponse(): Unit =
        runBlocking {
            setupRequestError()
            val response = sut.getVideos(query, key)
            assertNull(response.first().getOrNull()?.response)
            assertEquals(NetworkResponseErrorTypes.REQUEST_ERROR, response.first().getOrNull()?.error)
        }

    private fun setupSuccess() {
        runBlocking {
            whenever(dataSource.requestVideos(query = query, key = key)).thenReturn(
                flow {
                    emit(Result.success(dsSuccessfulResponse))
                }
            )
        }
        sut = GetYoutubeVideosRepository(dataSource)
    }

    private fun setupEmptyList() {
        runBlocking {
            whenever(dataSource.requestVideos(query, key)).thenReturn(
                flow {
                    emit(Result.success(emptyListResponse))
                }
            )
        }
        sut = GetYoutubeVideosRepository(dataSource)
    }

    private fun setupNetworkError() {
        runBlocking {
            whenever(dataSource.requestVideos(query, key)).thenReturn(
                flow {
                    emit(Result.failure(IOException()))
                }
            )
        }
        sut = GetYoutubeVideosRepository(dataSource)
    }

    private fun setupRequestError() {
        runBlocking {
            whenever(dataSource.requestVideos(query, key)).thenReturn(
                flow {
                    emit(Result.failure(RuntimeException()))
                }
            )
        }
        sut = GetYoutubeVideosRepository(dataSource)
    }
}