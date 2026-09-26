package com.example.smartmailbox.addmailbox.ui.steps

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartmailbox.addmailbox.domain.DeviceVerificationCodeFormat
import com.example.smartmailbox.addmailbox.ui.AddMailboxState
import com.example.smartmailbox.addmailbox.ui.DeviceVerificationView
import com.example.smartmailbox.addmailbox.ui.components.VerifyDeviceButton
import com.example.smartmailbox.ui.components.ErrorText

@Composable
fun DeviceVerificationStep(
    state: AddMailboxState,
    onCodeChange: (String) -> Unit,
    onVerify: () -> Unit,
) {
    Text(
        text = "Enter the code from your device",
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(Modifier.height(24.dp))

    DeviceVerificationView(
        verificationCode = state.deviceVerificationCode,
        onVerificationCodeChange = onCodeChange,
        enabled = !state.isLoading,
        onDone = onVerify
    )

    ErrorText(state.errorMessage)
    Spacer(Modifier.height(24.dp))

    VerifyDeviceButton(
        isLoading = state.isLoading,
        enabled = DeviceVerificationCodeFormat.hasRequiredLength(state.deviceVerificationCode),
        onVerifyButtonClick = onVerify
    )
}
