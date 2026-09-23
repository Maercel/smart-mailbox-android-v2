package com.example.smartmailbox.home.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartmailbox.R
import com.example.smartmailbox.ui.theme.Alata
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.LightGray

@Composable
fun AddNewMailboxButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(16.dp, 8.dp),
        elevation = ButtonDefaults.buttonElevation(),
        shape = RoundedCornerShape(5.dp),
        enabled = true,
        colors = ButtonDefaults.buttonColors(
            containerColor = LightGray,
            disabledContainerColor = LightGray,
            //disabledContentColor = Black.copy(alpha = 0.3f)
        )
    ) {
        Icon(
            modifier = Modifier
                .size(28.dp),
            painter = painterResource(R.drawable.add_crosshair),
            contentDescription = "Add new mailbox",
            tint = Black,
        )
    }
}