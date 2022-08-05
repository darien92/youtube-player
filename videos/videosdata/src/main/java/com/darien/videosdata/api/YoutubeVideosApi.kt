package com.darien.videosdata.api

import com.darien.videosdata.commons.Constants
import com.darien.videosdata.models.data.YoutubeVideosResponseModel
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface YoutubeVideosApi {
    @GET(value = Constants.SEARCH_DIR)
    fun getYoutubeVideos(
        @Query(Constants.QUERY_KEY)
        query: String,
        @Query(Constants.PART_KEY)
        part: String = Constants.SNIPPET,
        @Query(Constants.API_KEY_KEY)
        key: String,
        @Query(Constants.MAX_RESULTS_KEY)
        maxResults: Int = Constants.MAX_RESULTS,
        @Query(Constants.PAGE_TOKEN)
        pageToken: String
    ): YoutubeVideosResponseModel
}