package com.darien.videosui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.darien.videosui.viewmodels.VideosViewModel


@Composable
fun VideosScreen(
    navController: NavController,
    videoName: String,
    viewModel: VideosViewModel = hiltViewModel()
) {
    Text(videoName)
}