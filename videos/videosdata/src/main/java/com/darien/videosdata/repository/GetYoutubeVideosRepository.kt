package com.darien.videosdata.repository

import com.darien.core.network.NetworkResponseErrorTypes
import com.darien.videosdata.datasource.GetYoutubeVideosDataSource
import com.darien.videosdata.models.domain.ResponseOrError
import com.darien.videosdata.models.domain.YoutubeVideoDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetYoutubeVideosRepository @Inject constructor(private val dataSource: GetYoutubeVideosDataSource) {
    private var pageToken = ""

    suspend fun getVideos(query: String, key: String): Flow<Result<ResponseOrError>> {
        val dsResponse = dataSource.requestVideos(query = query, key = key, pageToken = pageToken)
        if (dsResponse.first().isSuccess && dsResponse.first().getOrNull() != null) {
            if (dsResponse.first().getOrNull()!!.items != null) {val videos: MutableList<YoutubeVideoDomainModel> = ArrayList()
                pageToken = dsResponse.first().getOrNull()!!.nextPageToken.toString()
                val videosData = dsResponse.first().getOrNull()!!.items!!
                for (currVideoData in videosData) {
                    videos.add(
                        YoutubeVideoDomainModel(
                            id = currVideoData.id?.videoId.toString(),
                            publishedAt = currVideoData.snippet?.publishedAt.toString(),
                            title = currVideoData.snippet?.title.toString(),
                            description = currVideoData.snippet?.description.toString(),
                            thumbnail = currVideoData.snippet?.thumbnails?.medium?.url.toString(),
                            channelName = currVideoData.snippet?.channelTitle.toString()
                        )
                    )
                }
                return flow {
                    emit(Result.success(ResponseOrError(response = videos)))
                }
            }
            return flow {
                emit(Result.success(ResponseOrError(error = NetworkResponseErrorTypes.REQUEST_ERROR)))
            }
        } else {
            val exception = dsResponse.first().exceptionOrNull()
            if (exception != null && exception is IOException) {
                return flow {
                    emit(Result.success(ResponseOrError(error = NetworkResponseErrorTypes.NETWORK_ERROR)))
                }
            }
            return flow {
                emit(Result.success(ResponseOrError(error = NetworkResponseErrorTypes.REQUEST_ERROR)))
            }
        }
    }
}