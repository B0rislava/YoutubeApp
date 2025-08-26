package com.example.youtubeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.youtubeapp.app.YoutubeApp
import com.example.youtubeapp.ui.theme.YoutubeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YoutubeAppTheme {
                YoutubeApp()
            }
        }
    }
}
