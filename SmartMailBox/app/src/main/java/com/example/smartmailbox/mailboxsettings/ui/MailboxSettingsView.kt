package com.example.smartmailbox.mailboxsettings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartmailbox.ui.components.ScreenHeader
import com.example.smartmailbox.ui.theme.Black

@Composable
fun MailboxSettingsView(
    paddingValues: PaddingValues,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        ScreenHeader(title = "Mailbox Settings", onBack = onBack)
        HorizontalDivider(color = Black.copy(alpha = 0.1f))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center) {
            Text(
                text = "Coming soon",
                style = MaterialTheme.typography.bodyLarge,
                // alpha = 0.7f
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}