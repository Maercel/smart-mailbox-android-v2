package com.example.smartmailbox.addmailbox.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartmailbox.addmailbox.ui.AddMailboxState

@Composable
fun AddMailboxButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onAddButtonClick: () -> Unit
) {
    Button(
        onClick = {
            onAddButtonClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(0.dp, 8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(5.dp),
        enabled = !isLoading
    ) {

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text("Add Mailbox")
        }
    }
}