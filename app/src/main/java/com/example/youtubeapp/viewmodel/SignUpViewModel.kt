package com.example.youtubeapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
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

    fun submit() {
        if (validate()) {

        }
    }
}
