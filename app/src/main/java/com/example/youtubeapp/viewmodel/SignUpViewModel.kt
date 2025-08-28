package com.example.youtubeapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.youtubeapp.repository.FakeRepository

class SignUpViewModel : ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)

    private val repository = FakeRepository()

    fun validate(): Boolean {
        var valid = true

        if (!email.contains("@") || !email.contains(".")) {
            emailError = "Invalid email"
            valid = false
        } else {
            emailError = null
        }

        if (password.length < 6) {
            passwordError = "Password too short"
            valid = false
        } else {
            passwordError = null
        }

        return valid
    }

    fun submit(onSuccess: () -> Unit) {
        if (validate()) {
            val result = repository.signUp(name, email, password)
            if (result) {
                onSuccess()
            } else {
                emailError = "Signup failed"
            }
        }
    }
}
