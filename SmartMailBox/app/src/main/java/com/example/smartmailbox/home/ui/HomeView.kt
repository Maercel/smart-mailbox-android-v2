package com.example.smartmailbox.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartmailbox.R
import com.example.smartmailbox.home.domain.Mailbox
import com.example.smartmailbox.home.domain.MailboxDeviceCatalog
import com.example.smartmailbox.home.domain.MailboxDeviceModel
import com.example.smartmailbox.home.ui.components.AddNewMailboxButton
import com.example.smartmailbox.ui.theme.Black


@Composable
fun HomeView(
    homeViewModel: HomeViewModel,
    paddingValues: PaddingValues,
    navigateToAddMailboxScreen: () -> Unit,
    onMailboxClick: (mailboxId: String) -> Unit
) {
    val uiState by homeViewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp,8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        strokeWidth = 2.dp
                    )
                }
            }
            is HomeUiState.Error -> {
                Box(Modifier
                        .fillMaxWidth()
                        .padding(16.dp,8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Something went wrong: ${state.message}")
                }
            }
            is HomeUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    items(state.mailboxes, key = { it.id }) { mailbox ->
                        MailboxCard(
                            mailbox = mailbox,
                            onClick = { onMailboxClick(mailbox.id) }
                        )
                    }
                }
            }
        }

        AddNewMailboxButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            onClick = { navigateToAddMailboxScreen() }
        )
    }
}