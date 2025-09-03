package com.example.youtubeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.youtubeapp.data.local.DatabaseProvider
import com.example.youtubeapp.repository.UserRepository
import com.example.youtubeapp.screens.HomeScreen
import com.example.youtubeapp.screens.ProfileScreen
import com.example.youtubeapp.screens.SignInScreen
import com.example.youtubeapp.screens.SignUpScreen
import com.example.youtubeapp.viewmodel.ProfileViewModel
import com.example.youtubeapp.viewmodel.SignInViewModel
import com.example.youtubeapp.viewmodel.SignUpViewModel
import com.example.youtubeapp.viewmodel.factory.ProfileViewModelFactory
import com.example.youtubeapp.viewmodel.factory.SignInViewModelFactory
import com.example.youtubeapp.viewmodel.factory.SignUpViewModelFactory
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.youtubeapp.screens.SubscriptionsScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val userRepository = UserRepository(DatabaseProvider.getDatabase(context).userDao())

    NavHost(
        navController = navController,
        startDestination = Screen.SignUp.route
    ) {

        composable(Screen.SignUp.route) {
            val signUpViewModel: SignUpViewModel = viewModel(
                factory = SignUpViewModelFactory(context)
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
                factory = SignInViewModelFactory(context)
            )
            SignInScreen(
                viewModel = signInViewModel,
                onSignInSuccess = {
                    navController.navigate("${Screen.Home.route}/${signInViewModel.email}")
                },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }

        composable(
            route = "${Screen.Home.route}/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            HomeScreen(
                currentUserEmail = email,
                navController = navController
            )
        }

        composable(
            route = "${Screen.Profile.route}/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val profileViewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModelFactory(email, userRepository)
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

    }
}
