package com.darien.youtubeplayer.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.darien.core.navigation.YoutubePlayerScreens
import com.darien.youtubeplayer.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    LaunchedEffect(key1 = true){
        delay(2000L)
        navController.popBackStack()
        navController.navigate(YoutubePlayerScreens.SearchScreen.name)
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .height(180.dp)
                .width(250.dp),
            painter = painterResource(id = R.drawable.ic_player_icon),
            contentDescription = stringResource(
                id = R.string.youtube_player_icon_description
            ),
        )
    }
}