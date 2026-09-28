package com.example.smartmailbox.home.data

import com.example.smartmailbox.addmailbox.data.DeviceDto
import com.example.smartmailbox.mailboxdetail.domain.ActivityEvent
import com.example.smartmailbox.mailboxdetail.domain.ActivityType
import com.example.smartmailbox.mailboxdetail.domain.LockState
import kotlinx.coroutines.flow.Flow


interface MailboxRepository {
    fun observeMailboxes(): Flow<List<MailboxDto>>
    fun observeMailbox(id: String): Flow<MailboxDto?>
    fun observeActivity(mailboxId: String): Flow<List<ActivityEvent>>
    suspend fun setLockState(mailboxId: String, lockState: LockState, event: ActivityType): Result<Unit>
    suspend fun setLockdownState(mailboxId: String, enabled: Boolean): Result<Unit>
    suspend fun addMailbox(name: String, deviceVerificationCode: String): Result<Unit>
    suspend fun updateMailbox(dto: MailboxDto): Result<Unit>
    suspend fun deleteMailbox(id: String): Result<Unit>

    suspend fun verifyDevice(deviceVerificationCode: String): Result<DeviceDto>
}