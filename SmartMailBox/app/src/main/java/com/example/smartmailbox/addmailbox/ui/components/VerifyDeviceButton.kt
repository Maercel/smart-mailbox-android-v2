package com.example.smartmailbox.addmailbox.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.LightGray

@Composable
fun VerifyDeviceButton(
    isLoading: Boolean,
    enabled: Boolean,
    onVerifyButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isClickable = enabled && !isLoading

    Button(
        onClick = onVerifyButtonClick,
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(vertical = 8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(5.dp),
        enabled = isClickable,
        colors = ButtonDefaults.buttonColors(
            containerColor = LightGray,
            contentColor = Black,
            disabledContainerColor = LightGray,
            disabledContentColor = Black.copy(alpha = 0.4f),
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp,
                color = Black
            )
        } else {
            Text(
                text = "Verify Device",
                color = if (isClickable) Black else Black.copy(alpha = 0.4f)
            )
        }
    }
}