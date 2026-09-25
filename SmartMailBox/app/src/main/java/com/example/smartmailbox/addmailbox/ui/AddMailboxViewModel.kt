package com.example.smartmailbox.addmailbox.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.addmailbox.domain.AddMailboxEvent
import com.example.smartmailbox.addmailbox.domain.MailboxDeviceModelGenerator
import com.example.smartmailbox.home.data.FirestoreMailboxRepository
import com.example.smartmailbox.home.data.MailboxRepository
import com.example.smartmailbox.home.domain.Mailbox
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddMailboxViewModel(
    //private val repository: MailboxRepository
) : ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestoreMailboxRepository: FirestoreMailboxRepository = FirestoreMailboxRepository(firestore, firebaseAuth)
    private val _addMailboxState = MutableStateFlow(AddMailboxState())
    val addMailboxState: StateFlow<AddMailboxState> = _addMailboxState.asStateFlow()

    private val _events = MutableSharedFlow<AddMailboxEvent>()

    val events = _events.asSharedFlow()

    fun onLabelChange(label: String) {
        _addMailboxState.update { it.copy(label = label, errorMessage = null) }
    }

    fun addMailbox() {
        val name = _addMailboxState.value.label.trim()

        if (name.isEmpty()) {
            _addMailboxState.update { it.copy(errorMessage = "Mailbox label is required") }
            return
        }

        //DEMO
        val deviceModelId = MailboxDeviceModelGenerator.generate().id

        viewModelScope.launch {
            _addMailboxState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = firestoreMailboxRepository.addMailbox(name, deviceModelId)
            result.fold(
                onSuccess = {
                    _addMailboxState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    _events.emit(AddMailboxEvent.MailboxAdded)
                },
                onFailure = { e ->
                    _addMailboxState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Failed to add mailbox"
                        )
                    }
                }
            )
        }
    }

}