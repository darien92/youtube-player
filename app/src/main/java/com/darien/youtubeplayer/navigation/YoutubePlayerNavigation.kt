package com.darien.youtubeplayer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.darien.core.navigation.YoutubePlayerScreens
import com.darien.ui.screens.SearchScreen
import com.darien.videosui.VideosScreen
import com.darien.youtubeplayer.screen.SplashScreen

@Composable
fun YoutubePlayerNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = YoutubePlayerScreens.SplashScreen.name
    ) {
        composable(YoutubePlayerScreens.SplashScreen.name) {
            SplashScreen(
                navController = navController
            )
        }

        composable(YoutubePlayerScreens.SearchScreen.name) {
            SearchScreen(
                navController = navController
            )
        }

        composable(YoutubePlayerScreens.VideosScreen.name + "/{query}") { backStackEntry ->
            backStackEntry.arguments?.getString("query")
                ?.let { VideosScreen(navController = navController, videoName = it) }
        }
    }
}