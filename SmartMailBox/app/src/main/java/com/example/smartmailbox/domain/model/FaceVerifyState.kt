package com.example.smartmailbox.domain.model

data class FaceVerifyState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isVerified: Boolean = false,
    val isCameraStarted: Boolean = false
)