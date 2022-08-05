package com.darien.videosdata.models.domain

import com.darien.core.network.NetworkResponseErrorTypes

data class ResponseOrError(
    val response: List<YoutubeVideoDomainModel>? = null,
    val error: NetworkResponseErrorTypes? = null
)
