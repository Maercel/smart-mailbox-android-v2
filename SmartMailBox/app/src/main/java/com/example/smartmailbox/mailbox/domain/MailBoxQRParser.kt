package com.example.smartmailbox.mailbox.domain

object MailBoxQRParser {

    fun extractMailBoxId(url: String): Int {
        val segments = url.trimEnd('/').split("/")

        if (segments.size <= 4) {
            throw IllegalArgumentException("Invalid mailbox QR URL")
        }

        return segments[4].toIntOrNull()
            ?: throw IllegalArgumentException("Invalid mailbox ID")
    }
}