package com.example.youtubeapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeapp.data.local.entities.User
import com.example.youtubeapp.repository.UserRepository
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)

    fun validate(): Boolean {
        var valid = true
        if (!email.contains("@") || !email.contains(".")) {
            emailError = "Invalid email"
            valid = false
        } else emailError = null

        if (password.length < 6) {
            passwordError = "Password too short"
            valid = false
        } else passwordError = null

        return valid
    }

    fun submit(onSuccess: () -> Unit) {
        if (!validate()) return

        viewModelScope.launch {
            val user = User(name = name, email = email, password = password)
            val result = repository.registerUser(user)
            if(result) onSuccess() else emailError = "Signup failed"
        }
    }
}
