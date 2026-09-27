package com.example.smartmailbox.mailboxdetail.ui

import androidx.annotation.DrawableRes
import com.example.smartmailbox.mailboxdetail.domain.LockState

data class MailboxDetailState(
    val name: String = "",
    @DrawableRes val imageRes: Int = 0,
    val batteryPercent: Int = 0,
    val isConnected: Boolean = false,
    val lockState: LockState = LockState.LOCKED,
    // Meaning: waiting for answer from server
    val isLockPending: Boolean = false,
    val isLockdown: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)