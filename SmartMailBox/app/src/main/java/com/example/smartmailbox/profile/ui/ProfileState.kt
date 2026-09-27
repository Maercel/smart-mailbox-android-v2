package com.example.smartmailbox.profile.ui

data class ProfileState(
    val name: String = "",
    val email: String = "",
    // Google sign-in accounts don't have password
    val hasPassword: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)