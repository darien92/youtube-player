package com.darien.videosui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun VideosScreen(
    navController: NavController,
    videoName: String
) {
    Text(videoName)
}