package com.example.smartmailbox.mailboxdetail.ui

import android.util.Log
import android.util.Log.e
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.home.data.FirestoreMailboxRepository
import com.example.smartmailbox.home.domain.toDomain
import com.example.smartmailbox.mailboxdetail.domain.LockState
import com.example.smartmailbox.navigation.ui.NavigationScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class MailboxDetailViewModel(
    // navigation fills in extracted arguments before viewmodel is created
    // arrives pre-filled with mailboxId -> ble584
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mailboxId: String =
        checkNotNull(savedStateHandle[NavigationScreen.MailboxDetail.ARG_MAILBOX_ID])

    private val _mailboxDetailState: MutableStateFlow<MailboxDetailState> = MutableStateFlow(MailboxDetailState())
    val mailboxDetailState = _mailboxDetailState.asStateFlow()

    private val repository = FirestoreMailboxRepository(
        FirebaseFirestore.getInstance(),
        FirebaseAuth.getInstance()
    )

    // SIMULATION ONLY
    private val OPEN_CHANCE = 0.4f // simulates the person is there
    private val RANDOM_OPEN_DELAY_MS = 3_000L..10_000L

    private var doorSimulation: Job? = null

    init {
        viewModelScope.launch {
            repository.observeMailbox(mailboxId)
                .catch { e ->
                    Log.e("MailboxDetail", "observeMailbox failed for $mailboxId", e)
                    _mailboxDetailState.update {
                        it.copy(isLoading = false, errorMessage = "Couldn't load this mailbox.")
                    }
                }
                .collect { dto ->
                    if (dto == null) {
                        _mailboxDetailState.update {
                            it.copy(isLoading = false, errorMessage = "This mailbox no longer exists.")
                        }
                        return@collect
                    }
                    val mailbox = dto.toDomain()
                    _mailboxDetailState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            name = mailbox.name,
                            imageRes = mailbox.imageRes,
                            batteryPercent = mailbox.batteryPercent,
                            isConnected = mailbox.isConnected,
                        )
                    }
                }
        }
    }

    fun onLockButtonClick() {
        if (_mailboxDetailState.value.isLockPending) return

        when (_mailboxDetailState.value.lockState) {
            LockState.LOCKED -> unlock()
            LockState.UNLOCKED -> lock()
            LockState.OPEN -> Unit    // can't lock open mailbox
        }
    }

    //TODO: Add actual firebase logic
    private fun unlock() {
        viewModelScope.launch {
            _mailboxDetailState.update { it.copy(isLockPending = true) }
            delay(500.milliseconds) // DEMO: firebase responds
            _mailboxDetailState.update { it.copy(isLockPending = false, lockState = LockState.UNLOCKED) }

            doorSimulation = launch { simulateDoor() }
        }
    }

    private fun lock() {
        doorSimulation?.cancel()
        viewModelScope.launch {
            _mailboxDetailState.update { it.copy(isLockPending = true) }
            delay(500.milliseconds)
            _mailboxDetailState.update { it.copy(isLockPending = false, lockState = LockState.LOCKED) }
        }
    }

    // DEMO: someone might open the doors
    private suspend fun simulateDoor() {
        if (Random.nextFloat() >= OPEN_CHANCE) return // DEMO: nobody opened it

        _mailboxDetailState.update { it.copy(lockState = LockState.OPEN) }
        delay(RANDOM_OPEN_DELAY_MS.random().milliseconds) //DEMO: door opened for some time
        _mailboxDetailState.update { it.copy(lockState = LockState.LOCKED) }  // auto-lock back
    }
}