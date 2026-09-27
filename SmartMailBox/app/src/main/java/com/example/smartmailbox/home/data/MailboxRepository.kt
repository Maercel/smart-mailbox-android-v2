package com.example.smartmailbox.home.data

import com.example.smartmailbox.addmailbox.data.DeviceDto
import kotlinx.coroutines.flow.Flow


interface MailboxRepository {
    fun observeMailboxes(): Flow<List<MailboxDto>>
    fun observeMailbox(id: String): Flow<MailboxDto?>
    suspend fun addMailbox(name: String, deviceVerificationCode: String): Result<Unit>
    suspend fun updateMailbox(dto: MailboxDto): Result<Unit>
    suspend fun deleteMailbox(id: String): Result<Unit>

    suspend fun verifyDevice(deviceVerificationCode: String): Result<DeviceDto>
}