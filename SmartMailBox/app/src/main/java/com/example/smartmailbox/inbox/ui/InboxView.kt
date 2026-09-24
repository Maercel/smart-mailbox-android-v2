package com.example.smartmailbox.inbox.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartmailbox.R
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.VeryDarkGreen

@Composable
fun InboxView(
    paddingValues: PaddingValues,
    onBackButton: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            IconButton(
                onClick = onBackButton,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = stringResource(R.string.back_button),
                    tint = Black,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Inbox",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "No notifications",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Black.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    HorizontalDivider(
                        color = Black.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}