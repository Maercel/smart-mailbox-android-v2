package com.example.smartmailbox.home.data

import androidx.annotation.DrawableRes

data class MailboxDto(
    val id: String = "",
    val name: String = "",
    val batteryPercent: Int = 0,
    val isConnected: Boolean = false,
    val deviceModelId: String = "unknown"
    /*
    val imageUrl: String = "",       // firebase storage url
    val imageStoragePath: String = "" // "mailboxes/{id}/photo.jpg"
    */
)
