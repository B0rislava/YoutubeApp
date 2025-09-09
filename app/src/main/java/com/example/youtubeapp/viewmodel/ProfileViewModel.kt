package com.example.youtubeapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeapp.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository, private val email: String) : ViewModel() {

    val name = mutableStateOf("")
    val userEmail = mutableStateOf("")
    private var userId: Int? = null
    private var currentPassword: String? = null


    init {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email)
            user?.let {
                name.value = it.name
                userEmail.value = it.email
                userId = it.uid
                currentPassword = it.password
            }
        }
    }

    fun updateProfile(newName: String, newEmail: String, newPassword: String) {
        viewModelScope.launch {
            userId?.let { uid ->
                val updatedUser = com.example.youtubeapp.data.local.entities.User(
                    uid = uid,
                    name = newName,
                    email = newEmail,
                    password = newPassword.ifBlank { currentPassword ?: "" }
                )
                repository.updateUser(updatedUser)

                name.value = updatedUser.name
                userEmail.value = updatedUser.email
                currentPassword = updatedUser.password
            }
        }
    }
}
