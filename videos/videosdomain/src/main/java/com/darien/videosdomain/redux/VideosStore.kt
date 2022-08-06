package com.darien.videosdomain.redux

import com.darien.core.redux.Store
import com.darien.videosdomain.data.VideosActions
import com.darien.videosdomain.data.VideosViewState
import javax.inject.Inject

class VideosStore @Inject constructor(reducer: VideosReducer) :
    Store<VideosViewState, VideosActions>(
        initialState = VideosViewState(
            error = null,
            isLoading = false,
            isFirstLoading = true
        ), reducer = reducer
    )