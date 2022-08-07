package com.darien.videosdata.repository

import com.darien.core.network.NetworkResponseErrorTypes
import com.darien.videosdata.datasource.GetYoutubeVideosDataSource
import com.darien.videosdata.models.domain.ResponseOrError
import com.darien.videosdata.models.domain.YoutubeVideoDomainModel
import java.io.IOException
import javax.inject.Inject

class GetYoutubeVideosRepository @Inject constructor(private val dataSource: GetYoutubeVideosDataSource) {
    private var pageToken = ""

    suspend fun getVideos(query: String, key: String): Result<ResponseOrError> {
        val dsResponse = dataSource.requestVideos(query = query, key = key, pageToken = pageToken)
        if (dsResponse.isSuccess && dsResponse.getOrNull() != null) {
            if (dsResponse.getOrNull()!!.items != null) {
                val videos: MutableList<YoutubeVideoDomainModel> = ArrayList()
                pageToken = dsResponse.getOrNull()!!.nextPageToken.toString()
                val videosData = dsResponse.getOrNull()!!.items!!
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
                return Result.success(ResponseOrError(response = videos))
            }
            return Result.success(ResponseOrError(error = NetworkResponseErrorTypes.REQUEST_ERROR))
        } else {
            val exception = dsResponse.exceptionOrNull()
            if (exception != null && exception is IOException) {
                return Result.success(ResponseOrError(error = NetworkResponseErrorTypes.NETWORK_ERROR))
            }
            return Result.success(ResponseOrError(error = NetworkResponseErrorTypes.REQUEST_ERROR))
        }
    }
}