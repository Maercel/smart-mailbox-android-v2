package com.example.smartmailbox.home.data

import androidx.annotation.DrawableRes

data class MailboxDto(
    val id: String = "",
    val ownerId: String = "",
    val name: String = "",
    val deviceModelId: String = "unknown",
    val deviceVerificationCode: String = "",
    val lockState: String = "LOCKED",
    val lockdown: Boolean = false,
)
