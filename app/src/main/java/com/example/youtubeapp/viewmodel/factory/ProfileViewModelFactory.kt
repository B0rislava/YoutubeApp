package com.example.youtubeapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.youtubeapp.repository.UserRepository
import com.example.youtubeapp.viewmodel.ProfileViewModel

class ProfileViewModelFactory(
    private val email: String,
    private val repository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository, email) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
