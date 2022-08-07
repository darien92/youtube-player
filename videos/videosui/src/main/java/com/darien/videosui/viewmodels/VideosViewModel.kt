package com.darien.videosui.viewmodels

import com.darien.core.viewmodel.BaseViewModel
import com.darien.videosdomain.data.VideosActions
import com.darien.videosdomain.data.VideosViewState
import com.darien.videosdomain.redux.VideosStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class VideosViewModel @Inject constructor(
    store: VideosStore,
    dispatcher: CoroutineDispatcher
) :
    BaseViewModel<VideosViewState, VideosActions>(store = store, dispatcher = dispatcher) {

    fun loadVideosInitially(query: String, key: String) {
        handleAction(VideosActions.StartLoading)
        handleAction(VideosActions.LoadVideos(query, key))
        handleAction(VideosActions.StopLoading)
    }

    fun loadVideos(query: String, key: String) {
        handleAction(VideosActions.StartLoadingFirstTime)
        handleAction(VideosActions.LoadVideos(query, key))
        handleAction(VideosActions.StopLoadingFirstTime)
    }
}