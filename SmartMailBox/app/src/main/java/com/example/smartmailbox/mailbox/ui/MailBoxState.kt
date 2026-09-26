package com.example.smartmailbox.mailbox.ui

import com.example.smartmailbox.addmailbox.ui.AddMailboxStep

data class MailBoxState(
    val isMailBoxOpen: Boolean = false,
    val isUnlockSoundPlaying: Boolean = false,
    val error: String? = null
)
