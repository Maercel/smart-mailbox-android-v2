package com.example.smartmailbox.home.data

import kotlinx.coroutines.flow.Flow


interface MailboxRepository {
    fun observeMailboxes(): Flow<List<MailboxDto>>
    suspend fun addMailbox(name: String, deviceModelId: String): Result<Unit>
    suspend fun updateMailbox(dto: MailboxDto): Result<Unit>
    suspend fun deleteMailbox(id: String): Result<Unit>
}