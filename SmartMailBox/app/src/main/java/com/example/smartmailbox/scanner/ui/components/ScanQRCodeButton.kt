package com.example.smartmailbox.scanner.ui.components

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

@Composable
fun ScanQRCodeButton(modifier: Modifier = Modifier,
        onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(5.dp)
    ) {
        Text("Scan QR Code")
    }
}