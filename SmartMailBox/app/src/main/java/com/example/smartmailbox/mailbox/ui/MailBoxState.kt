package com.example.smartmailbox.mailbox.ui

data class MailBoxState(
    val isMailBoxOpen: Boolean = false,
    val isUnlockSoundPlaying: Boolean = false,
    val error: String? = null
)
