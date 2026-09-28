package com.example.smartmailbox.home.domain

import androidx.annotation.DrawableRes
import com.example.smartmailbox.home.data.MailboxDto
import com.example.smartmailbox.mailboxdetail.domain.LockState

private const val DEMO_BATTERY_PERCENT = 100
private const val DEMO_IS_CONNECTED = true

data class Mailbox(
    val id: String,
    val name: String,
    val batteryPercent: Int,
    val isConnected: Boolean,
    val deviceModel: MailboxDeviceModel,
    val lockState: LockState,
    // isLockdown here and lockdown in MailboxDto because firestore's mapping inconsistently because of is prefix.
    val isLockdown: Boolean,
    @DrawableRes val imageRes: Int
)

fun MailboxDto.toDomain(): Mailbox {
    val model = MailboxDeviceModel.getMailboxDeviceModelFromId(deviceModelId)
    return Mailbox(
        id = id,
        name = name,
        batteryPercent = DEMO_BATTERY_PERCENT, //TODO: get from device!
        isConnected = DEMO_IS_CONNECTED,
        deviceModel = model,
        imageRes = MailboxDeviceCatalog.imageFor(model),
        lockState = LockState.entries.find { it.name == lockState } ?: LockState.LOCKED,
        isLockdown = lockdown
    )
}