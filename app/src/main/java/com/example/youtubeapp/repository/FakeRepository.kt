package com.example.youtubeapp.repository

class FakeRepository {
    fun signUp(name: String, email: String, password: String): Boolean {
        return name.isNotBlank() && email.contains("@") && password.length >= 6
    }
}
