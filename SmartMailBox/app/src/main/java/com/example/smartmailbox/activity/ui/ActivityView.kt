package com.example.smartmailbox.activity.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp


@Composable
fun ActivityView(activityViewModel: ActivityViewModel, paddingValues: PaddingValues) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Coming soon",
            style = MaterialTheme.typography.bodyLarge,
            // alpha = 0.7f
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(12.dp))

        Text(
            style = MaterialTheme.typography.bodyLarge,
            text = buildAnnotatedString {
                append("To see mailbox activity, open ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Mailbox")
                }
                append(" and tap ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Activity")
                }
                append(".")
            },
            // alpha = 0.7f
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            // If the screen is small -> each line is on its own line, therefore center is needed. Otherwise the text is at the start.
            textAlign = TextAlign.Center
        )
    }
}