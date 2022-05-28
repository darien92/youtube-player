package com.darien.youtubeplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.darien.youtubeplayer.navigation.YoutubePlayerNavigation
import com.darien.youtubeplayer.ui.theme.YoutubePlayerTheme

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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    YoutubePlayerTheme {
        YoutubePlayerNavigation()
    }
}