package com.darien.videosdata.models.domain

data class YoutubeVideoDomainModel(
    val id: String,
    val publishedAt: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val channelName: String
)