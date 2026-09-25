package com.example.smartmailbox.addmailbox.domain

sealed interface AddMailboxEvent {
    data object MailboxAdded: AddMailboxEvent
}