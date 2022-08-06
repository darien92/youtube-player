package com.darien.videosdomain.redux

import com.darien.core.network.NetworkResponseErrorTypes
import com.darien.core.redux.DomainError
import com.darien.core.redux.Reducer
import com.darien.videosdata.repository.GetYoutubeVideosRepository
import com.darien.videosdomain.data.VideosActions
import com.darien.videosdomain.data.VideosViewState
import kotlinx.coroutines.flow.first
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
                if (response.first().getOrNull()?.response != null) {
                    prevState.videos.addAll(response.first().getOrNull()?.response!!)
                    return prevState.copy(isLoading = false)
                } else if (response.first().getOrNull()?.error != null && response.first()
                        .getOrNull()?.error == NetworkResponseErrorTypes.NETWORK_ERROR
                ) {
                    return prevState.copy(
                        error = DomainError(
                            errorCode = 0,
                            errorMessage = "Network error"
                        )
                    )
                } else {
                    return prevState.copy(
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