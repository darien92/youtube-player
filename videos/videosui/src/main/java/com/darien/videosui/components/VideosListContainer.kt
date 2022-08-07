package com.darien.videosui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darien.videosdata.models.domain.YoutubeVideoDomainModel

@Composable
fun VideosListContainer(
    modifier: Modifier = Modifier,
    videos: List<YoutubeVideoDomainModel>,
    isLoading: Boolean,
    onListReachesEnd: ()-> Unit,
    onVideoClicked: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LazyColumn {
            itemsIndexed(videos) { index, currVideo ->
                if (index == videos.lastIndex) {
                    onListReachesEnd()
                }
                VideoInListComponent(videoDomainModel = currVideo) { videoId ->
                    onVideoClicked.invoke(videoId)
                }
            }
        }
        if (isLoading) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}