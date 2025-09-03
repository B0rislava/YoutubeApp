package com.example.youtubeapp.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeapp.repository.UserRepository
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: UserRepository) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var loginError by mutableStateOf<String?>(null)

    fun validateCredentials(): Boolean {
        var isValid = true

        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email"
            isValid = false
        } else emailError = null

        if (password.isBlank()) {
            passwordError = "Password cannot be empty"
            isValid = false
        } else passwordError = null

        return isValid
    }

    fun login(onSuccess: () -> Unit) {
        if (!validateCredentials()) return

        viewModelScope.launch {
            val user = repository.login(email, password)
            if(user != null){
                onSuccess()
            } else {
                loginError = "Invalid credentials"
            }
        }
    }
}