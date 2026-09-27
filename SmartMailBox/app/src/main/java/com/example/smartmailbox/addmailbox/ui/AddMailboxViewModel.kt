package com.example.smartmailbox.addmailbox.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.addmailbox.data.DeviceAlreadyClaimedException
import com.example.smartmailbox.addmailbox.data.DeviceNotFoundException
import com.example.smartmailbox.addmailbox.domain.AddMailboxEvent
import com.example.smartmailbox.addmailbox.domain.DeviceVerificationCodeFormat
import com.example.smartmailbox.addmailbox.domain.MailboxDeviceModelGenerator
import com.example.smartmailbox.home.data.FirestoreMailboxRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
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

    private val firestoreMailboxRepository = FirestoreMailboxRepository(
        FirebaseFirestore.getInstance(),
        FirebaseAuth.getInstance()
    )
    private val _addMailboxState = MutableStateFlow(AddMailboxState())
    val addMailboxState: StateFlow<AddMailboxState> = _addMailboxState.asStateFlow()

    private val _events = MutableSharedFlow<AddMailboxEvent>()

    val events = _events.asSharedFlow()

    fun onVerificationCodeChange(input: String) {
        _addMailboxState.update { it.copy(deviceVerificationCode = DeviceVerificationCodeFormat.normalize(input), errorMessage = null) }
    }

    fun onLabelChange(value: String) {
        _addMailboxState.update { it.copy(label = value, errorMessage = null) }
    }

    fun onVerifyClick() {
        val currentAddMailboxState = _addMailboxState.value
        if (currentAddMailboxState.isLoading) return

        if (!DeviceVerificationCodeFormat.hasRequiredLength(currentAddMailboxState.deviceVerificationCode)) {
            _addMailboxState.update {
                it.copy(errorMessage = "Enter the full ${DeviceVerificationCodeFormat.LENGTH}-character code")
            }
            return
        }

        viewModelScope.launch {
            _addMailboxState.update { it.copy(isLoading = true, errorMessage = null) }
            firestoreMailboxRepository.verifyDevice(currentAddMailboxState.deviceVerificationCode)
                .onSuccess {
                    _addMailboxState.update {
                        it.copy(isLoading = false, addMailboxStep = AddMailboxStep.MailboxSetup)
                    }
                }
                .onFailure { e ->
                    _addMailboxState.update {
                        it.copy(isLoading = false, errorMessage = e.toUserMessage())
                    }
                }
        }
    }

    fun onBackToVerification() {
        _addMailboxState.update {
            it.copy(addMailboxStep = AddMailboxStep.DeviceVerification, errorMessage = null)
        }
    }

    /*
    fun onPasswordChange(value: String) {
        _addMailboxState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _addMailboxState.update { it.copy(confirmPassword = value, errorMessage = null) }
    }
    */
    fun addMailbox() {
        val currentAddMailboxState = _addMailboxState.value
        if (currentAddMailboxState.isLoading) return

        val name = currentAddMailboxState.label.trim()
        if (name.isEmpty()) {
            _addMailboxState.update { it.copy(errorMessage = "Mailbox label is required") }
            return
        }

        viewModelScope.launch {
            _addMailboxState.update { it.copy(isLoading = true, errorMessage = null) }
            firestoreMailboxRepository.addMailbox(deviceVerificationCode = currentAddMailboxState.deviceVerificationCode, name = name)
                .onSuccess {
                    _addMailboxState.update { it.copy(isLoading = false) }
                    _events.emit(AddMailboxEvent.MailboxAdded)
                }
                .onFailure { e ->
                    _addMailboxState.update {
                        it.copy(isLoading = false, errorMessage = e.toUserMessage())
                    }
                }
        }
    }

    private fun Throwable.toUserMessage(): String = when (this) {
        is DeviceNotFoundException -> "No device found with this code"
        is DeviceAlreadyClaimedException -> "This device is already registered"
        is FirebaseFirestoreException ->
            if (code == FirebaseFirestoreException.Code.UNAVAILABLE) "No connection. Check your internet and try again."
            else "Something went wrong. Try again."
        else -> "Something went wrong. Try again."
    }
}