package com.example.smartmailbox.addmailbox.ui

data class AddMailboxState(
    val label: String = "",
    /*
    val password: String = "",
    val confirmPassword: String = "",
    val selectedGroups: Set<String> = emptySet(),
    */
    val deviceVerificationCode: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val addMailboxStep: AddMailboxStep = AddMailboxStep.DeviceVerification,
)