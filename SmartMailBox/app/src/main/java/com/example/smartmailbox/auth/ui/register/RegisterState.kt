package com.example.smartmailbox.auth.ui.register

data class RegisterState(
    //val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)