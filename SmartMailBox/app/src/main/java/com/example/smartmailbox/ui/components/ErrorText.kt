package com.example.smartmailbox.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartmailbox.ui.theme.Alata
import com.example.smartmailbox.ui.theme.ErrorRed

@Composable
fun ErrorText(message: String?) {
    message ?: return
    Spacer(Modifier.height(12.dp))
    Text(
        text = message,
        fontFamily = Alata,
        fontSize = 14.sp,
        color = ErrorRed,
        fontWeight = FontWeight.Bold
    )
}