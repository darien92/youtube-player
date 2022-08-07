package com.darien.videosdata.models.domain

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

data class YoutubeVideoDomainModel(
    val id: String,
    val publishedAt: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val channelName: String
) {
    @SuppressLint("SimpleDateFormat")
    fun formatDate(strDate: String = publishedAt): String {
        val subStr = strDate.substring(startIndex = 0, endIndex = 10)
        val date = SimpleDateFormat("yyyy-MM-dd").parse(subStr)
        val format = SimpleDateFormat("MMM, dd - yyyy")
        return format.format(date!!)
    }
}