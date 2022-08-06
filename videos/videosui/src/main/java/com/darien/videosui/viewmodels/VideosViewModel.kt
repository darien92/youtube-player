package com.darien.videosui.viewmodels

import com.darien.core.viewmodel.BaseViewModel
import com.darien.videosdomain.data.VideosActions
import com.darien.videosdomain.data.VideosViewState
import com.darien.videosdomain.redux.VideosStore
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class VideosViewModel @Inject constructor(
    store: VideosStore,
    dispatcher: CoroutineDispatcher
) :
    BaseViewModel<VideosViewState, VideosActions>(store, dispatcher) {

    fun startLoading() {
        handleAction(VideosActions.StartLoading)
    }

    fun stopLoading() {
        handleAction(VideosActions.StopLoading)
    }

    fun startLoadingInitially() {
        handleAction(VideosActions.StartLoadingFirstTime)
    }

    fun stopLoadingInitially() {
        handleAction(VideosActions.StopLoadingFirstTime)
    }

    fun searchVideos(query: String, key: String) {
        handleAction(VideosActions.LoadVideos(query, key))
    }
}