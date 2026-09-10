package com.example.smartmailbox.api.data

data class MobileLoginResponse(
    val _id: String? = null,
    val username: String? = null,
    val email: String? = null,
    val twoFactorEnabled: Boolean? = null,
    val twoFactorRequired: Boolean? = null,
    val message: String? = null
)