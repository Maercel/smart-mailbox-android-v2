package com.example.smartmailbox.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.home.data.FirestoreMailboxRepository
import com.example.smartmailbox.home.data.MailboxRepository
import com.example.smartmailbox.home.domain.Mailbox
import com.example.smartmailbox.home.domain.MailboxDeviceCatalog
import com.example.smartmailbox.home.domain.MailboxDeviceModel
import com.example.smartmailbox.home.domain.toDomain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val mailboxes: List<Mailbox>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel() : ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val repository: FirestoreMailboxRepository = FirestoreMailboxRepository(firestore, firebaseAuth)

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val demoModel = MailboxDeviceModel.MEDIUM_V2
    private val mailboxDemo =
        Mailbox(
            id = "1",
            name = "Demo Mailbox",
            batteryPercent = 87,
            isConnected = true,
            deviceModel = demoModel,
            imageRes = MailboxDeviceCatalog.imageFor(demoModel)
        )



    init {
        observeMailboxes()
    }

    private fun demoData() {
        _uiState.update {
            HomeUiState.Success(
                    mailboxes = listOf(mailboxDemo)
            )
        }
    }

    private fun observeMailboxes() {
        viewModelScope.launch {
            repository.observeMailboxes()
                .map { dtos -> dtos.map { it.toDomain() } }
                .catch { e ->
                    _uiState.value = HomeUiState.Error(e.message ?: "Failed to load mailboxes")
                }
                .collect { mailboxes ->
                    _uiState.value = HomeUiState.Success(mailboxes)
                }
        }
    }

    fun onMailboxCardClick(id: String) {
        // TODO: navigate to details
    }
}