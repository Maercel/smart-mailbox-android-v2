package com.example.smartmailbox.mailbox.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartmailbox.mailbox.ui.MailBoxViewModel
import com.example.smartmailbox.ui.theme.Alata
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.LightGray

@Composable
fun PlayAgainButton(
    modifier: Modifier = Modifier,
    mailBoxViewModel: MailBoxViewModel,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(0.dp, 8.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (
                mailBoxViewModel.mailBoxState.isUnlockSoundPlaying
            ) 2.dp else 6.dp
        ),
        shape = RoundedCornerShape(5.dp),
        enabled = !mailBoxViewModel.mailBoxState.isUnlockSoundPlaying,
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = LightGray,
            //disabledContentColor = Black.copy(alpha = 0.3f)
        )
    ) {
        Text(
            "Play Again",
            fontFamily = Alata,
            fontWeight = FontWeight.Bold,
            color = Black
        )
    }
}