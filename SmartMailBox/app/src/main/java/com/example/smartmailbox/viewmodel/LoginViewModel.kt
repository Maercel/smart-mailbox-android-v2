package com.example.smartmailbox.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.api.AuthRetrofitInstance
import com.example.smartmailbox.api.MobileLoginRequest
import com.example.smartmailbox.auth.AuthRepository
import com.example.smartmailbox.model.LoginState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject

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
                        isLoggedIn = true,
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

    fun loginWithGoogle() {

    }

    fun clearLoginNavigationFlags() {
        loginState = loginState.copy(
            isLoggedIn = false,
        )
    }
}