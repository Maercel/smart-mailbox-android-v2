package com.example.smartmailbox.home.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreMailboxRepository(
    private val firestore: FirebaseFirestore
) : MailboxRepository {

    private val collection = firestore.collection("mailboxes")

    override fun observeMailboxes(): Flow<List<MailboxDto>> = callbackFlow {
        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val mailboxes = snapshot?.toObjects(MailboxDto::class.java) ?: emptyList()
            trySend(mailboxes)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun addMailbox(name: String, deviceModelId: String): Result<Unit> = runCatching {
        val id = collection.document().id
        val dto = MailboxDto(
            id = id,
            name = name,
            batteryPercent = 100,
            isConnected = true,
            deviceModelId = deviceModelId
        )
        collection.document(id).set(dto).await()
        Unit
    }

    override suspend fun updateMailbox(dto: MailboxDto): Result<Unit> = runCatching {
        collection.document(dto.id).set(dto).await()
        Unit
    }

    override suspend fun deleteMailbox(id: String): Result<Unit> = runCatching {
        collection.document(id).delete().await()
        Unit
    }
}