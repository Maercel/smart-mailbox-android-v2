package com.example.smartmailbox.mailboxdetail.ui

import android.util.Log
import android.util.Log.e
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.home.data.FirestoreMailboxRepository
import com.example.smartmailbox.home.domain.toDomain
import com.example.smartmailbox.navigation.ui.NavigationScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MailboxDetailViewModel(
    // navigation fills in extracted arguments before viewmodel is created
    // arrives pre-filled with mailboxId -> ble584
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mailboxId: String =
        checkNotNull(savedStateHandle[NavigationScreen.MailboxDetail.ARG_MAILBOX_ID])

    private val _mailboxDetailState: MutableStateFlow<MailboxDetailUiState> = MutableStateFlow(MailboxDetailUiState())
    val mailboxDetailState = _mailboxDetailState.asStateFlow()

    private val repository = FirestoreMailboxRepository(
        FirebaseFirestore.getInstance(),
        FirebaseAuth.getInstance()
    )

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
}