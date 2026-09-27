package com.example.smartmailbox.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.smartmailbox.R
import com.example.smartmailbox.ui.theme.Black

@Composable
fun ArrowForwardIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(R.drawable.arrow_forward),
        contentDescription = "Arrow forward",
        tint = Black.copy(alpha = 0.5f),
        modifier = modifier
            .size(16.dp)
    )
}