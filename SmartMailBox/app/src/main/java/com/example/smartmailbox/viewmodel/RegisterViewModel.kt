package com.example.smartmailbox.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.api.AuthRetrofitInstance
import com.example.smartmailbox.api.RegisterRequest
import com.example.smartmailbox.auth.AuthRepository
import com.example.smartmailbox.model.RegisterState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject

class RegisterViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    var registerState by mutableStateOf(RegisterState())
        private set

    fun onUsernameChange(username: String) {
        registerState = registerState.copy(
            username = username,
            errorMessage = null
        )
    }

    fun onEmailChange(email: String) {
        registerState = registerState.copy(
            email = email,
            errorMessage = null
        )
    }

    fun onPasswordChange(password: String) {
        registerState = registerState.copy(
            password = password,
            errorMessage = null
        )
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        registerState = registerState.copy(
            confirmPassword = confirmPassword,
            errorMessage = null
        )
    }

    fun register() {
        val username = registerState.username.trim()
        val email = registerState.email.trim()
        val password = registerState.password
        val confirmPassword = registerState.confirmPassword

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            registerState = registerState.copy(
                errorMessage = "All fields are required"
            )
            return
        }

        if (password != confirmPassword) {
            registerState = registerState.copy(
                errorMessage = "Passwords do not match"
            )
            return
        }

        viewModelScope.launch {
            registerState = registerState.copy(
                isLoading = true,
                errorMessage = null,
                isRegistered = false
            )

            try {
                val registerResult =
                    authRepository.registerAccount(username, email, password)

                registerState = registerState.copy(
                    isLoading = false,
                    isRegistered = true
                )
            } catch (e: FirebaseAuthException) {
                registerState = registerState.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Registration failed."
                )
            } catch (e: Exception) {
                registerState = registerState.copy(
                    isLoading = false,
                    errorMessage = "System error: ${e.message}"
                )
            }
        }
    }

    fun clearRegisterNavigationFlag() {
        registerState = registerState.copy(
            isRegistered = false
        )
    }
}