package com.example.smartmailbox.addmailbox.ui

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartmailbox.R
import com.example.smartmailbox.addmailbox.domain.AddMailboxEvent
import com.example.smartmailbox.addmailbox.ui.components.AddMailboxButton
import com.example.smartmailbox.ui.theme.Alata
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.ErrorRed
import com.example.smartmailbox.ui.theme.LightGray
import com.example.smartmailbox.ui.theme.VeryDarkGreen

// NOTE: Device password + verification code (hashed) were left
// out. Hashing correctly requires a Cloud Function, which requires the Blaze
// plan (not paying that). Dropping this also means the device model/icon is now assigned by
// MailboxDeviceModelGenerator instead of user input — see FirestoreMailboxRepository.addMailbox.
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
                AddMailboxEvent.MailboxAdded -> { onMailboxAdded() }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    )
    {
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
                text = "Add Mailbox",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = addMailboxState.label,
                onValueChange = addMailboxViewModel::onLabelChange,
                label = { Text("Mailbox Label") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !addMailboxState.isLoading,
                shape = RoundedCornerShape(5.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VeryDarkGreen, // COLORS MATE!
                    unfocusedBorderColor = VeryDarkGreen,
                    focusedLabelColor = VeryDarkGreen,
                    cursorColor = VeryDarkGreen
                )
            )
            /*
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = addMailboxState.password,
                onValueChange = addMailboxViewModel::onPasswordChange,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !addMailboxState.isLoading,
                shape = RoundedCornerShape(5.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VeryDarkGreen,
                    unfocusedBorderColor = VeryDarkGreen,
                    focusedLabelColor = VeryDarkGreen,
                    cursorColor = VeryDarkGreen
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = addMailboxState.confirmPassword,
                onValueChange = addMailboxViewModel::onConfirmPasswordChange,
                label = { Text("Repeat Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !addMailboxState.isLoading,
                shape = RoundedCornerShape(5.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VeryDarkGreen,
                    unfocusedBorderColor = VeryDarkGreen,
                    focusedLabelColor = VeryDarkGreen,
                    cursorColor = VeryDarkGreen
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = addMailboxState.verificationCode,
                onValueChange = addMailboxViewModel::onVerificationCodeChange,
                label = { Text("Verification Code") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !addMailboxState.isLoading,
                shape = RoundedCornerShape(5.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VeryDarkGreen,
                    unfocusedBorderColor = VeryDarkGreen,
                    focusedLabelColor = VeryDarkGreen,
                    cursorColor = VeryDarkGreen
                )
            )
            */

            addMailboxState.errorMessage?.let { errorMessage ->
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = errorMessage,
                    fontFamily = Alata,
                    fontSize = 14.sp,
                    color = ErrorRed,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            AddMailboxButton(
                modifier = Modifier,
                isLoading = addMailboxState.isLoading,
                onAddButtonClick = {
                    addMailboxViewModel.addMailbox()
                }
            )
        }
    }
}