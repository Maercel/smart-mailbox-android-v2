package com.example.smartmailbox.addmailbox.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartmailbox.R
import com.example.smartmailbox.addmailbox.domain.AddMailboxEvent
import com.example.smartmailbox.addmailbox.domain.DeviceVerificationCodeFormat
import com.example.smartmailbox.addmailbox.ui.components.AddMailboxButton
import com.example.smartmailbox.addmailbox.ui.components.VerifyDeviceButton
import com.example.smartmailbox.addmailbox.ui.steps.MailboxSetupStep
import com.example.smartmailbox.addmailbox.ui.steps.DeviceVerificationStep
import com.example.smartmailbox.ui.components.ErrorText
import com.example.smartmailbox.ui.components.ScreenHeader
import com.example.smartmailbox.ui.theme.Alata
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.ErrorRed

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

    // system back
    BackHandler(enabled = addMailboxState.addMailboxStep == AddMailboxStep.MailboxSetup) {
        addMailboxViewModel.onBackToVerification()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        ScreenHeader(
            title = if (addMailboxState.addMailboxStep == AddMailboxStep.MailboxSetup) "Add Mailbox" else "Verify Device",
            onBack = {
                if (addMailboxState.addMailboxStep == AddMailboxStep.MailboxSetup) addMailboxViewModel.onBackToVerification() else onBackButton()
            }
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
