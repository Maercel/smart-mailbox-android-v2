package com.example.smartmailbox.view.components.buttons

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartmailbox.ui.theme.Alata
import com.example.smartmailbox.viewmodel.MailBoxViewModel

@Composable
fun OpenMailBoxButton(modifier: Modifier = Modifier,
                      mailBoxViewModel: MailBoxViewModel,
                      onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(0.dp, 8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(5.dp),
        enabled = !mailBoxViewModel.apiState.isLoading
    ) {
        if (mailBoxViewModel.apiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text("Open Mailbox",
                fontFamily = Alata,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

