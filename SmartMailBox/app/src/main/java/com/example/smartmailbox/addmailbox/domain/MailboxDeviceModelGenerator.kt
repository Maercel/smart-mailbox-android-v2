package com.example.smartmailbox.addmailbox.domain

import com.example.smartmailbox.home.domain.MailboxDeviceModel
import kotlin.random.Random

object MailboxDeviceModelGenerator {

    fun generate(): MailboxDeviceModel {
        val sizeRoll = Random.nextInt(0, 301)

        return when {
            sizeRoll <= 99 -> MailboxDeviceModel.SMALL
            sizeRoll <= 119 -> MailboxDeviceModel.MEDIUM_V2
            sizeRoll <= 199 -> MailboxDeviceModel.MEDIUM
            else -> MailboxDeviceModel.LARGE
        }
    }
}