package com.darien.videosdomain.data

import com.darien.core.redux.Action

sealed class VideosActions : Action {
    object StartLoadingFirstTime : VideosActions()
    object StopLoadingFirstTime : VideosActions()
    object StartLoading : VideosActions()
    object StopLoading : VideosActions()
    data class LoadVideos(val query: String, val key: String) : VideosActions()
}