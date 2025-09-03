package com.example.youtubeapp.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.youtubeapp.data.local.DatabaseProvider
import com.example.youtubeapp.repository.UserRepository
import com.example.youtubeapp.viewmodel.SignInViewModel

class SignInViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            val userDao = DatabaseProvider.getDatabase(context).userDao()
            val repository = UserRepository(userDao)
            @Suppress("UNCHECKED_CAST")
            return SignInViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
