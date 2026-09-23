package com.example.smartmailbox.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartmailbox.R
import com.example.smartmailbox.home.domain.Mailbox
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.LightGray


@Composable
fun MailboxCard(
    mailbox: Mailbox,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(LightGray)
            .clickable { onClick() }
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = mailbox.imageRes),
                contentDescription = mailbox.name,
                modifier = Modifier.size(72.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.width(8.dp))

            Column {
                Text(
                    text = mailbox.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_battery_android_full),
                        contentDescription = "Battery percentage",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("${mailbox.batteryPercent}%")
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(Color.White)
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(if (mailbox.isConnected) "Connected" else "Disconnected")

            Icon(
                painter = painterResource(R.drawable.arrow_forward),
                contentDescription = "Arrow forward",
                tint = Black.copy(alpha = .5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}