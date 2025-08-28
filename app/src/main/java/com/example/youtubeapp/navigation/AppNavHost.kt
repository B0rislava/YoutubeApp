package com.example.youtubeapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.youtubeapp.screens.HomeScreen
import com.example.youtubeapp.screens.SignInScreen
import com.example.youtubeapp.screens.SignUpScreen
import com.example.youtubeapp.viewmodel.SignUpViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SignUp.route
    ) {
        composable(Screen.SignUp.route) {
            val vm: SignUpViewModel = viewModel()
            SignUpScreen(
                viewModel = vm,
                onSignUpSuccess = { navController.navigate(Screen.Home.route) },
                navController = navController
            )
        }

        composable(Screen.SignIn.route) {
            SignInScreen(
                onSignInSuccess = { navController.navigate(Screen.Home.route) },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}
