package com.darien.videosdomain.data

import com.darien.core.redux.DomainError
import com.darien.core.redux.State
import com.darien.videosdata.models.domain.YoutubeVideoDomainModel

data class VideosViewState(
    override val error: DomainError?,
    override var isLoading: Boolean,
    var isFirstLoading: Boolean,
    val videos: ArrayList<YoutubeVideoDomainModel> = ArrayList()
) : State