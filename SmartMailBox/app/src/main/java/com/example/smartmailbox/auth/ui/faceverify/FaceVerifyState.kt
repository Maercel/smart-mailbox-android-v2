package com.example.smartmailbox.auth.ui.faceverify

data class FaceVerifyState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isVerified: Boolean = false,
    val isCameraStarted: Boolean = false
)