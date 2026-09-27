package com.example.smartmailbox.addmailbox.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartmailbox.addmailbox.domain.AddMailboxEvent
import com.example.smartmailbox.addmailbox.ui.steps.DeviceVerificationStep
import com.example.smartmailbox.addmailbox.ui.steps.MailboxSetupStep
import com.example.smartmailbox.ui.components.ScreenHeader
import com.example.smartmailbox.ui.theme.Black

@Composable
fun AddMailboxView(
    addMailboxViewModel: AddMailboxViewModel,
    paddingValues: PaddingValues,
    onBackButton: () -> Unit,
    onMailboxAdded: () -> Unit,
) {
    val addMailboxState by addMailboxViewModel.addMailboxState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        addMailboxViewModel.events.collect { event ->
            when (event) {
                AddMailboxEvent.MailboxAdded -> onMailboxAdded()
            }
        }
    }

    // System back
    BackHandler(enabled = addMailboxState.addMailboxStep == AddMailboxStep.MailboxSetup) {
        addMailboxViewModel.onBackToVerification()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        ScreenHeader(
            title = when (addMailboxState.addMailboxStep) {
                AddMailboxStep.DeviceVerification -> "Verify Device"
                AddMailboxStep.MailboxSetup -> "Add Mailbox"
            },
            onBack = {
                when (addMailboxState.addMailboxStep) {
                    AddMailboxStep.DeviceVerification -> onBackButton()
                    AddMailboxStep.MailboxSetup -> addMailboxViewModel.onBackToVerification()
                }
            }
        )

        HorizontalDivider(
            color = Black.copy(alpha = 0.1f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (addMailboxState.addMailboxStep) {
                AddMailboxStep.DeviceVerification -> DeviceVerificationStep(
                    state = addMailboxState,
                    onCodeChange = addMailboxViewModel::onVerificationCodeChange,
                    onVerify = addMailboxViewModel::onVerifyClick
                )
                AddMailboxStep.MailboxSetup -> MailboxSetupStep(
                    state = addMailboxState,
                    onLabelChange = addMailboxViewModel::onLabelChange,
                    onAdd = addMailboxViewModel::addMailbox
                )
            }
        }
    }
}