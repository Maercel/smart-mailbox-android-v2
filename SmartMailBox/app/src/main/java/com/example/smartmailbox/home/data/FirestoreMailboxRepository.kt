package com.example.smartmailbox.home.data

import android.util.Log
import com.example.smartmailbox.addmailbox.domain.MailboxDeviceModelGenerator
import com.example.smartmailbox.auth.domain.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

class FirestoreMailboxRepository(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : MailboxRepository {
    private val collection = firestore.collection("mailboxes")

    private val authRepository = AuthRepository(firebaseAuth, firestore)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeMailboxes(): Flow<List<MailboxDto>> =
        authRepository.currentUserIdFlow.flatMapLatest { uid ->
            if (uid == null) flowOf(emptyList()) else mailboxesOwnedBy(uid)
        }

    private fun mailboxesOwnedBy(uid: String): Flow<List<MailboxDto>> = callbackFlow {
        val registration = collection
            .whereEqualTo("ownerId", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    val loggedOutMidFlight =
                        error.code == FirebaseFirestoreException.Code.PERMISSION_DENIED &&
                                authRepository.currentUserId!= uid
                    if (loggedOutMidFlight) close() else close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObjects(MailboxDto::class.java).orEmpty())
            }
        awaitClose { registration.remove() }
    }


    override suspend fun addMailbox(name: String, deviceModelId: String): Result<Unit> = runCatching {
        val id = collection.document().id

        val userId = authRepository.currentUserId
            ?: throw IllegalStateException("User is not authenticated")

        val dto = MailboxDto(
            id = id,
            ownerId = userId,
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