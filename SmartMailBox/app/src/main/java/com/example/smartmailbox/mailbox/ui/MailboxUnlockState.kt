package com.example.smartmailbox.mailbox.ui

sealed interface MailboxUnlockState {
    data object Loading : MailboxUnlockState
    data object Unlocked : MailboxUnlockState
    data object Error : MailboxUnlockState
}