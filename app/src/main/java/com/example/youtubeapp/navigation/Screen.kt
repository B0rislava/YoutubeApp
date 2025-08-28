package com.example.youtubeapp.navigation

sealed class Screen(val route: String) {
    data object SignUp : Screen("signup")
    data object SignIn : Screen("signin")
    data object Home   : Screen("home")

}
