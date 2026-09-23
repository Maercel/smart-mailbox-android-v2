package com.example.smartmailbox.home.domain

import androidx.annotation.DrawableRes
import com.example.smartmailbox.home.data.MailboxDto

data class Mailbox(
    val id: String,
    val name: String,
    val batteryPercent: Int,
    val isConnected: Boolean,
    val deviceModel: MailboxDeviceModel,
    @DrawableRes val imageRes: Int
)

fun MailboxDto.toDomain(): Mailbox {
    val model = MailboxDeviceModel.getMailboxDeviceModelFromId(deviceModelId)
    return Mailbox(
        id = id,
        name = name,
        batteryPercent = batteryPercent,
        isConnected = isConnected,
        deviceModel = model,
        imageRes = MailboxDeviceCatalog.imageFor(model)
    )
}

fun Mailbox.toDto(): MailboxDto = MailboxDto(
    id = id,
    name = name,
    batteryPercent = batteryPercent,
    isConnected = isConnected,
    deviceModelId = deviceModel.id
)