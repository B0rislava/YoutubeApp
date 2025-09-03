package com.example.youtubeapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeapp.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository, private val email: String) : ViewModel() {
    val name = mutableStateOf("")
    val userEmail = mutableStateOf("")

    init {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email)
            user?.let {
                name.value = it.name
                userEmail.value = it.email
            }
        }
    }
}
