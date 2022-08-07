package com.darien.videosui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.darien.videosdata.models.domain.YoutubeVideoDomainModel

@Composable
fun VideoInListComponent(
    modifier: Modifier = Modifier,
    videoDomainModel: YoutubeVideoDomainModel,
    onClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
            .clickable { onClick.invoke(videoDomainModel.id) }) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            painter = rememberImagePainter(videoDomainModel.thumbnail),
            contentDescription = videoDomainModel.title
        )
        VideoDetails(
            title = videoDomainModel.title,
            channelName = videoDomainModel.channelName,
            publishedAt = videoDomainModel.formatDate()
        )
    }
}

@Composable
fun VideoDetails(
    modifier: Modifier = Modifier,
    title: String,
    channelName: String,
    publishedAt: String
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp)
    ) {
        val subtitle = "$channelName * $publishedAt"
        Text(
            style = MaterialTheme.typography.body1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            text = title
        )
        Text(
            style = MaterialTheme.typography.caption,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            text = subtitle
        )
    }
}