package com.example.smartmailbox.mailboxactivity.ui

import com.example.smartmailbox.mailboxdetail.domain.ActivityEvent

data class MailboxActivityState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val events: List<ActivityEvent> = emptyList(),
)