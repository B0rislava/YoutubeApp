package com.example.youtubeapp.app

import androidx.compose.runtime.Composable
import com.example.youtubeapp.navigation.AppNavHost
import com.example.youtubeapp.ui.theme.YoutubeAppTheme

@Composable
fun YoutubeApp() {
    YoutubeAppTheme {
        AppNavHost()
    }
}
