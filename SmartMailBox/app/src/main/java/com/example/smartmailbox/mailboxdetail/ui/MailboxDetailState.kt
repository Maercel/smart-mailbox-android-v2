package com.example.smartmailbox.mailboxdetail.ui

import androidx.annotation.DrawableRes

data class MailboxDetailUiState(
    val name: String = "",
    @DrawableRes val imageRes: Int = 0,
    val batteryPercent: Int = 0,
    val isConnected: Boolean = false,
    val isUnlocking: Boolean = false,
    val isUnlocked: Boolean = false,
    val isLockdown: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)