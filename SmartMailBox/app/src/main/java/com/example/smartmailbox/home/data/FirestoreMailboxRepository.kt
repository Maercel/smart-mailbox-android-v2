package com.example.smartmailbox.home.data

import com.example.smartmailbox.addmailbox.data.DeviceAlreadyClaimedException
import com.example.smartmailbox.addmailbox.data.DeviceDto
import com.example.smartmailbox.addmailbox.data.DeviceNotFoundException
import com.example.smartmailbox.auth.domain.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Source
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

class FirestoreMailboxRepository(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : MailboxRepository {
    private val mailboxesCollection = firestore.collection("mailboxes")
    private val devicesCollection = firestore.collection("devices")

    private val authRepository = AuthRepository(firebaseAuth, firestore)

    override fun observeMailbox(id: String): Flow<MailboxDto?> = callbackFlow {
        val registration = mailboxesCollection.document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObject(MailboxDto::class.java))
            }
        awaitClose {
            registration.remove()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeMailboxes(): Flow<List<MailboxDto>> =
        authRepository.currentUserIdFlow.flatMapLatest { uid ->
            if (uid == null) flowOf(emptyList()) else mailboxesOwnedBy(uid)
        }

    private fun mailboxesOwnedBy(uid: String): Flow<List<MailboxDto>> = callbackFlow {
        val registration = mailboxesCollection
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


    override suspend fun addMailbox(name: String, deviceVerificationCode: String): Result<Unit> = runCatching {
        val uid = authRepository.currentUserId
            ?: throw IllegalStateException("User is not authenticated")

        val deviceRef = devicesCollection.document(deviceVerificationCode)
        val mailboxRef = mailboxesCollection.document()

        // we will make that static
        //val deviceModelId = MailboxDeviceModelGenerator.generate()

        // transaction NEEDED, since we don't want 2 users to claim the same device
        firestore.runTransaction { transaction ->
            val device = transaction.get(deviceRef).toObject(DeviceDto::class.java)
                ?: throw DeviceNotFoundException()
            if (device.claimedBy != null) throw DeviceAlreadyClaimedException()

            // should already exist so, update is the right call here
            transaction.update(
                deviceRef,
                mapOf("claimedBy" to uid, "mailboxId" to mailboxRef.id)
            )
            transaction.set(
                mailboxRef,
                mapOf(
                    "id" to mailboxRef.id,
                    "ownerId" to uid,
                    "name" to name,
                    "deviceModelId" to device.deviceModelId,
                    "deviceCode" to deviceVerificationCode,
                )
            )
        }.await()
        Unit
    }

    override suspend fun updateMailbox(dto: MailboxDto): Result<Unit> = runCatching {
        mailboxesCollection.document(dto.id).set(dto).await()
        Unit
    }

    override suspend fun deleteMailbox(id: String): Result<Unit> = runCatching {
        mailboxesCollection.document(id).delete().await()
        Unit
    }

    override suspend fun verifyDevice(deviceVerificationCode: String): Result<DeviceDto> = runCatching {
        val snapshot = devicesCollection.document(deviceVerificationCode)
            .get(Source.SERVER)          // never trust cache for this
            .await()
        val device = snapshot.toObject(DeviceDto::class.java)
            ?: throw DeviceNotFoundException()
        if (device.claimedBy != null) throw DeviceAlreadyClaimedException()
        device
    }
}