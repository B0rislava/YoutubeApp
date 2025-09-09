package com.example.youtubeapp.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.youtubeapp.data.local.DatabaseProvider
import com.example.youtubeapp.repository.UserRepository
import com.example.youtubeapp.viewmodel.ProfileViewModel
import com.example.youtubeapp.viewmodel.SignInViewModel
import com.example.youtubeapp.viewmodel.SignUpViewModel

class AppViewModelFactory(
    private val context: Context,
    private val email: String? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val userDao = DatabaseProvider.getDatabase(context).userDao()
        val repository = UserRepository(userDao)

        return when {
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                requireNotNull(email) { "Email is required for ProfileViewModel" }
                ProfileViewModel(repository, email) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
