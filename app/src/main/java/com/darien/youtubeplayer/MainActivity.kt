package com.darien.youtubeplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.darien.youtubeplayer.navigation.YoutubePlayerNavigation
import com.darien.youtubeplayer.ui.theme.YoutubePlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YoutubePlayerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    YoutubePlayerNavigation()
                }
            }
        }
    }
}
