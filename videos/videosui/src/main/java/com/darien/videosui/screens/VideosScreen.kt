package com.darien.videosui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.darien.videosui.components.LoadingView
import com.darien.videosui.components.NavigationView
import com.darien.videosui.components.VideosListContainer
import com.darien.videosui.viewmodels.VideosViewModel


@Composable
fun VideosScreen(
    navController: NavController,
    videoName: String,
    viewModel: VideosViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val apiKey = stringResource(id = com.darien.videosdata.R.string.youtube_api_key)

    LaunchedEffect(key1 = state.videos) {
        if (state.videos.isEmpty()) {
            viewModel.loadVideosInitially(query = videoName, key = apiKey)
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        NavigationView {
            navController.popBackStack()
        }
        if (state.videos.isNotEmpty()) {
            VideosListContainer(
                videos = state.videos,
                isLoading = state.isLoading,
                onListReachesEnd = { viewModel.loadVideos(query = videoName, key = apiKey) }
            ) {
                //TODO: Handle Video click if needed
            }
        } else if (state.isFirstLoading) {
            LoadingView()
        } else if (state.error != null) {
            Text(text = "error")
        }
    }
}