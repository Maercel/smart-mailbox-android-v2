package com.example.smartmailbox.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartmailbox.ui.components.ArrowForwardIcon
import com.example.smartmailbox.profile.ui.components.SignOutButton
import com.example.smartmailbox.ui.components.ScreenHeader
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.LightGray

@Composable
fun ProfileView(
    profileViewModel: ProfileViewModel,
    paddingValues: PaddingValues,
    onBack: () -> Unit,
    onEditProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onNotificationSettingsClick: () -> Unit,
) {
    val profileState by profileViewModel.profileState.collectAsStateWithLifecycle()
    var showSignOutDialog by remember { mutableStateOf(false) }

    // TODO: Remake into Lazy column!
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        ScreenHeader(title = "Profile", onBack = onBack)

        // To create that nice depth effect
        HorizontalDivider(color = Black.copy(alpha = 0.1f))

        // The list takes all the spaces, which leaves the sign out button to be at the bottom
        Column(Modifier.weight(1f)) {
            SettingsSectionHeader("Account")
            ProfileHeaderRow(email = profileState.email, onClick = onEditProfileClick)

            if (profileState.hasPassword) {
                HorizontalDivider(Modifier.padding(start = 16.dp),  color = Black.copy(alpha = 0.1f))

                SettingsRow(title = "Change password", onClick = onChangePasswordClick)
            }

            SettingsSectionHeader("Notifications")
            SettingsRow(title = "Notification settings", onClick = onNotificationSettingsClick)

            HorizontalDivider(color = Black.copy(alpha = 0.1f), modifier = Modifier.padding(start = 16.dp))
        }

        SignOutButton(
            onSignOutClick = { showSignOutDialog = true }
        )
    }

    if (showSignOutDialog) {
        SignOutDialog(
            onConfirm = {
                showSignOutDialog = false
                profileViewModel.logout()
            },
            onDismiss = { showSignOutDialog = false }
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    )
}

@Composable
private fun ProfileHeaderRow(email: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            //painter = painterResource(R.drawable.ic_account_circle),
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = "Account circle",
            tint = Black,
            modifier = Modifier.size(32.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = email,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )

        ArrowForwardIcon()
    }
}

@Composable
private fun SettingsRow(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .heightIn(min = 56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        ArrowForwardIcon()
    }
}

@Composable
private fun SignOutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        shape = RoundedCornerShape(5.dp),
        onDismissRequest = onDismiss,
        title = { Text("Sign out?", fontWeight = FontWeight.Bold) },
        text = { Text("Are you sure you want to sign out?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Sign out", color = Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Black, fontWeight = FontWeight.Normal)
            }
        }
    )
}