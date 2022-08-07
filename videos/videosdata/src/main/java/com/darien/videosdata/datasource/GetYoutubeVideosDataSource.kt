package com.darien.videosdata.datasource

import com.darien.videosdata.api.YoutubeVideosApi
import com.darien.videosdata.models.data.YoutubeVideosResponseModel
import javax.inject.Inject

class GetYoutubeVideosDataSource @Inject constructor(private val api: YoutubeVideosApi) {
    suspend fun requestVideos(
        query: String,
        key: String,
        pageToken: String = ""
    ): Result<YoutubeVideosResponseModel> {
        return try {
            val response = api.getYoutubeVideos(query = query, key = key, pageToken = pageToken)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}