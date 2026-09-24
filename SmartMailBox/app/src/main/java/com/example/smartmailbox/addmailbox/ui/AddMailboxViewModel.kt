package com.example.smartmailbox.addmailbox.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddMailboxViewModel : ViewModel() {


    private val _addMailboxState = MutableStateFlow(AddMailboxState())
    val addMailboxState: StateFlow<AddMailboxState> = _addMailboxState.asStateFlow()

    fun onVerificationCodeChange(verificationCode: String) {
        _addMailboxState.update {
            it.copy(verificationCode = verificationCode)
        }
    }

    fun onLabelChange(label: String) {
        _addMailboxState.update {
            it.copy(label = label)
        }
    }

    fun onPasswordChange(password: String) {
        _addMailboxState.update {
            it.copy(password = password)
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _addMailboxState.update {
            it.copy(confirmPassword = confirmPassword)
        }
    }

    fun addMailbox() {
        val label = _addMailboxState.value.label
        val password = _addMailboxState.value.password
        val confirmPassword = _addMailboxState.value.confirmPassword
        val verificationCode = _addMailboxState.value.verificationCode

        //TODO: validate input
    }

}