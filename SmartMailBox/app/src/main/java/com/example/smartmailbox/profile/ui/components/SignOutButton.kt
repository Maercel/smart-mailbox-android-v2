package com.example.smartmailbox.profile.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.LightGray


@Composable
fun SignOutButton(
    modifier: Modifier = Modifier,
    onSignOutClick: () -> Unit
) {
    Button(
        onClick = { onSignOutClick() },
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LightGray,
            contentColor = Black
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(56.dp),
        elevation = ButtonDefaults.buttonElevation(5.dp)
    ) {
        Text("Sign Out")
    }
}