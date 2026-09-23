package com.example.smartmailbox.home.domain

enum class MailboxDeviceModel(val id: String) {
    SMALL("small_v1"),
    MEDIUM("medium_v1"),
    MEDIUM_V2("medium_v2"),
    LARGE("large_v1"),
    UNKNOWN("unknown");

    companion object {
        fun getMailboxDeviceModelFromId(id: String): MailboxDeviceModel =
            entries.find { it.id == id } ?: UNKNOWN
    }
}