package com.example.smartmailbox.view

import androidx.collection.intSetOf
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartmailbox.ui.theme.Alata
import com.example.smartmailbox.ui.theme.Emerald
import com.example.smartmailbox.ui.theme.ErrorRed
import com.example.smartmailbox.ui.theme.ForestGreen
import com.example.smartmailbox.ui.theme.VeryDarkGreen
import com.example.smartmailbox.viewmodel.ProfileViewModel

@Composable
fun ProfileView(
    profileViewModel: ProfileViewModel,
    paddingValues: PaddingValues,
    onTwoFactorClick: () -> Unit
) {
    val profileState = profileViewModel.profileState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "User Profile",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProfileInfoRow(
            label = "Username:",
            value = profileState.username.ifBlank { "marcel" }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileInfoRow(
            label = "Email:",
            value = profileState.email.ifBlank { "marcel@gmail.com" }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileInfoRow(
            label = "2FA:",
            value = if (profileState.twoFactorEnabled) "Enabled" else "Disabled",
            valueColor = if (profileState.twoFactorEnabled) Emerald else ErrorRed
        )

        if (profileState.errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = profileState.errorMessage,
                fontFamily = Alata,
                fontSize = 14.sp,
                color = ErrorRed,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        /*
        Button(
            onClick = onTwoFactorClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ForestGreen,
                contentColor = VeryDarkGreen
            ),
            enabled = !profileState.isLoading
        ) {
            if (profileState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.height(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = if (profileState.twoFactorEnabled) "Disable 2FA" else "Enable 2FA",
                    fontFamily = Alata,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        */
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ForestGreen,
                contentColor = VeryDarkGreen
            )
        ) {
            Text(
                text = "Edit Profile",
                fontFamily = Alata,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { profileViewModel.logout() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Logout",
                fontFamily = Alata,
                style = MaterialTheme.typography.bodyMedium,
                color = VeryDarkGreen,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ProfileInfoRow(
    label: String,
    value: String,
    valueColor: Color = VeryDarkGreen // default color
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 0.dp, bottom = 5.dp)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = androidx.compose.ui.graphics.Color.White,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(horizontal = 12.dp, vertical = 14.dp)
        )
    }
}