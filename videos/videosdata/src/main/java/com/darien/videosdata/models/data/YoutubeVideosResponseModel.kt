package com.darien.videosdata.models.data

data class YoutubeVideosResponseModel(
    val etag: String?,
    val items: List<Item>?,
    val kind: String?,
    val nextPageToken: String?,
    val pageInfo: PageInfo?,
    val regionCode: String?
)