package com.example.smartmailbox.profile.ui

data class ProfileState(
    val username: String = "",
    val email: String = "",
    val twoFactorEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)