package com.example.smartmailbox.addmailbox.data

data class DeviceDto(
    val deviceModelId: String = "",
    val claimedBy: String? = null,    // null == unclaimed
    val mailboxId: String? = null,    // mailbox created when claimed
)

class DeviceNotFoundException : Exception("No device found with this code")
class DeviceAlreadyClaimedException : Exception("This device is already registered")