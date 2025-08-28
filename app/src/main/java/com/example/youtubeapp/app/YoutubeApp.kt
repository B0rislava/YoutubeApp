package com.example.youtubeapp.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.youtubeapp.navigation.Screen
import com.example.youtubeapp.navigation.YoutubeAppRouter
import com.example.youtubeapp.screens.SignUpScreen
import com.example.youtubeapp.viewmodel.SignUpViewModel
import com.example.youtubeapp.screens.HomeScreen


@Composable
fun YoutubeApp() {
    val signUpViewModel: SignUpViewModel = viewModel()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Crossfade(targetState = YoutubeAppRouter.currentScreen) { currentScreen ->
            when (currentScreen.value) {
                is Screen.SignUpScreen -> {
                    //SignUpScreen(viewModel = signUpViewModel)
                    HomeScreen()
                }
            }
        }
    }
}
