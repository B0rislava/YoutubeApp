package com.example.youtubeapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.youtubeapp.screens.HomeScreen
import com.example.youtubeapp.screens.ProfileScreen
import com.example.youtubeapp.screens.SignInScreen
import com.example.youtubeapp.screens.SignUpScreen
import com.example.youtubeapp.viewmodel.ProfileViewModel
import com.example.youtubeapp.viewmodel.SignInViewModel
import com.example.youtubeapp.viewmodel.SignUpViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.youtubeapp.model.VideoRepository
import com.example.youtubeapp.screens.SubscriptionsScreen
import com.example.youtubeapp.screens.VideoWatchScreen
import com.example.youtubeapp.utils.SessionManager
import com.example.youtubeapp.viewmodel.factory.AppViewModelFactory

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val savedEmail by SessionManager.getUserSession(context).collectAsState(initial = null)

    if(savedEmail == null){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
        return
    }

    val startDestination = if (savedEmail != null) {
        "${Screen.Home.route}/$savedEmail?tab=0"
    } else {
        Screen.SignIn.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Screen.SignUp.route) {
            val signUpViewModel: SignUpViewModel = viewModel(
                factory = AppViewModelFactory(context)
            )
            SignUpScreen(
                viewModel = signUpViewModel,
                onSignUpSuccess = {
                    navController.navigate(Screen.SignIn.route)
                },
                onNavigateToSignIn = { navController.navigate(Screen.SignIn.route) }
            )
        }

        composable(Screen.SignIn.route) {
            val signInViewModel: SignInViewModel = viewModel(
                factory = AppViewModelFactory(context)
            )
            SignInScreen(
                viewModel = signInViewModel,
                onSignInSuccess = {
                    navController.navigate("${Screen.Home.route}/${signInViewModel.email}?tab=0")
                },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }

        composable(
            route = "${Screen.Home.route}/{email}?tab={tab}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("tab")  { type = NavType.IntType; defaultValue = 0 }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val initialTab = backStackEntry.arguments?.getInt("tab") ?: 0
            HomeScreen(
                currentUserEmail = email,
                navController = navController,
                initialTab = initialTab
            )
        }

        composable(
            route = "${Screen.Profile.route}/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val profileViewModel: ProfileViewModel = viewModel(
                factory = AppViewModelFactory(context, email)
            )
            ProfileScreen(
                viewModel = profileViewModel,
                navController = navController
            )
        }

        composable(
            route = "${Screen.Subscriptions.route}/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            SubscriptionsScreen(
                currentUserEmail = email,
                navController = navController
            )
        }

        composable(
            route = "watch/{videoId}/{email}",
            arguments = listOf(
                navArgument("videoId") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("videoId") ?: return@composable
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val video = VideoRepository.videos.firstOrNull { it.id == id }
            if (video == null) {
                Text("Video not found", modifier = Modifier.padding(16.dp))
            } else {
                VideoWatchScreen(
                    video = video,
                    navController = navController,
                    currentUserEmail = email
                )
            }
        }
    }
}
