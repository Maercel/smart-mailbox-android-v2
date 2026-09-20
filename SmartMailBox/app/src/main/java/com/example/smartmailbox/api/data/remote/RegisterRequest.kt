package com.example.smartmailbox.api.data.remote

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)