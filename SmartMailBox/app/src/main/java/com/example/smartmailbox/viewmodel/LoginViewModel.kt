package com.example.smartmailbox.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.auth.AuthRepository
import com.example.smartmailbox.domain.model.LoginState
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    fun onEmailChange(email: String) {
        loginState = loginState.copy(
            email = email,
            errorMessage = null
        )
    }

    var loginState by mutableStateOf(LoginState())
        private set

    fun onPasswordChange(password: String) {
        loginState = loginState.copy(
            password = password,
            errorMessage = null
        )
    }

    fun login() {
        val email = loginState.email.trim()
        val password = loginState.password

        if (email.isEmpty() || password.isEmpty()) {
            loginState = loginState.copy(
                errorMessage = "Username and password are required"
            )
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginState = loginState.copy(errorMessage = "Enter a valid email address")
            return
        }

        loginState = loginState.copy(
            isLoading = true,
            errorMessage = null)

        viewModelScope.launch {
            try {
                val loginResult = authRepository.login(email, password)

                if (loginResult) {
                    loginState = loginState.copy(
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                loginState = loginState.copy(
                    isLoading = false,
                    errorMessage = "${e.message}"
                )
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        loginState = loginState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val success = authRepository.signInWithGoogleIdToken(idToken)
            loginState = loginState.copy(
                isLoading = false,
                errorMessage = if (!success)
                    "Google sign-in failed"
                else null
            )
        }
    }
}