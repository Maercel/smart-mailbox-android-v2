package com.example.smartmailbox.home.domain

import androidx.annotation.DrawableRes
import com.example.smartmailbox.R

object MailboxDeviceCatalog {
    private val deviceIconMap: Map<MailboxDeviceModel, Int> = mapOf(
        MailboxDeviceModel.SMALL to R.drawable.mailbox_small_v1,
        MailboxDeviceModel.MEDIUM to R.drawable.mailbox_medium_v1,
        MailboxDeviceModel.MEDIUM_V2 to R.drawable.mailbox_medium_v2,
        MailboxDeviceModel.LARGE to R.drawable.mailbox_large_v1
    )

    @DrawableRes
    fun imageFor(model: MailboxDeviceModel): Int =
        deviceIconMap[model] ?: deviceIconMap.getValue(MailboxDeviceModel.UNKNOWN)
}