package com.example.smartmailbox.addmailbox.ui.steps

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartmailbox.addmailbox.ui.AddMailboxState
import com.example.smartmailbox.addmailbox.ui.components.AddMailboxButton
import com.example.smartmailbox.ui.components.ErrorText
import com.example.smartmailbox.ui.theme.Black

@Composable
fun MailboxSetupStep(
    state: AddMailboxState,
    onLabelChange: (String) -> Unit,
    onAdd: () -> Unit,
) {
    OutlinedTextField(
        value = state.name,
        onValueChange = onLabelChange,
        label = { Text("Mailbox Name") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        enabled = !state.isLoading,
        shape = RoundedCornerShape(5.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Black,
            unfocusedBorderColor = Black,
            focusedLabelColor = Black,
            cursorColor = Black
        )
    )

    ErrorText(state.errorMessage)
    Spacer(Modifier.height(24.dp))

    AddMailboxButton(
        isLoading = state.isLoading,
        onAddButtonClick = onAdd
    )
}