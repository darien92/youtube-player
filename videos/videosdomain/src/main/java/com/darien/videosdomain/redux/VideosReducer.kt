package com.darien.videosdomain.redux

import com.darien.core.network.NetworkResponseErrorTypes
import com.darien.core.redux.DomainError
import com.darien.core.redux.Reducer
import com.darien.videosdata.repository.GetYoutubeVideosRepository
import com.darien.videosdomain.data.VideosActions
import com.darien.videosdomain.data.VideosViewState
import javax.inject.Inject

class VideosReducer @Inject constructor(private val repository: GetYoutubeVideosRepository) :
    Reducer<VideosViewState, VideosActions> {
    override suspend fun reduce(
        prevState: VideosViewState,
        action: VideosActions
    ): VideosViewState {
        return when (action) {
            is VideosActions.StartLoadingFirstTime -> {
                prevState.copy(isFirstLoading = true)
            }
            is VideosActions.StopLoadingFirstTime -> {
                prevState.copy(isFirstLoading = false)
            }
            is VideosActions.StartLoading -> {
                prevState.copy(isLoading = true)
            }
            is VideosActions.StopLoading -> {
                prevState.copy(isFirstLoading = true)
            }
            is VideosActions.LoadVideos -> {
                val response = repository.getVideos(query = action.query, key = action.key)
                if (response.getOrNull()?.response != null) {
                    val videosInList = prevState.videos
                    for (currVideo in response.getOrNull()!!.response!!) {
                        videosInList.add(currVideo)
                    }
                    prevState.copy(isLoading = false, videos = videosInList)
                } else if (response.getOrNull()?.error != null && response
                        .getOrNull()?.error == NetworkResponseErrorTypes.NETWORK_ERROR
                ) {
                    prevState.copy(
                        error = DomainError(
                            errorCode = 0,
                            errorMessage = "Network error"
                        )
                    )
                } else {
                    prevState.copy(
                        error = DomainError(
                            errorCode = 1,
                            errorMessage = "Request error"
                        )
                    )
                }
            }
        }
    }
}