package com.example.smartmailbox.mailboxdetail.domain

// Needed to log the activity
enum class ActivityType(val isUserAction: Boolean) {
    UNLOCKED(isUserAction = true),
    LOCKED(isUserAction = true),
    DOOR_OPENED(isUserAction = false),
    AUTO_LOCKED(isUserAction = false),
    LOCKDOWN_ON(isUserAction = true),
    LOCKDOWN_OFF(isUserAction = true)
}