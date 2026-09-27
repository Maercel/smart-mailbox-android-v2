package com.example.smartmailbox.mailboxactivity.ui

import android.os.Build
import android.text.format.DateUtils
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartmailbox.R
import com.example.smartmailbox.mailboxdetail.domain.ActivityEvent
import com.example.smartmailbox.mailboxdetail.domain.ActivityType
import com.example.smartmailbox.ui.components.ScreenHeader
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.LightGray

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MailboxActivityView(
    mailboxActivityViewModel: MailboxActivityViewModel,
    paddingValues: PaddingValues,
    onBack: () -> Unit,
) {
    val activityState by mailboxActivityViewModel.activityState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        ScreenHeader(title = "Activity", onBack = onBack)
        HorizontalDivider(color = Black.copy(alpha = 0.1f))

        when {
            activityState.isLoading -> CenteredBox {
                CircularProgressIndicator(color = LightGray)
            }

            activityState.errorMessage != null -> CenteredBox {
                Text(
                    text = activityState.errorMessage.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            activityState.events.isEmpty() -> CenteredBox {
                Text(
                    text = "No activity yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            else -> LazyColumn(Modifier.fillMaxSize()) {
                items(activityState.events, key = { it.id }) { event ->
                    ActivityRow(event)
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 56.dp),   // lines up with the text, not the icon
                        color = Black.copy(alpha = 0.1f)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ActivityRow(event: ActivityEvent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(event.type.iconRes()),
            contentDescription = null,
            tint = Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                text = event.type.label(),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = event.subtitle(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CenteredBox(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

// --- How each event type looks (UI decisions, so they live here, not in the domain) ---

private fun ActivityType.label(): String = when (this) {
    ActivityType.UNLOCKED -> "Unlocked"
    ActivityType.LOCKED -> "Locked"
    ActivityType.DOOR_OPENED -> "Door opened"
    ActivityType.AUTO_LOCKED -> "Locked automatically"
}

@DrawableRes
private fun ActivityType.iconRes(): Int = when (this) {
    ActivityType.UNLOCKED -> R.drawable.ic_unlocked_right
    ActivityType.LOCKED -> R.drawable.locked
    ActivityType.DOOR_OPENED -> R.drawable.ic_mailbox_door
    ActivityType.AUTO_LOCKED -> R.drawable.locked
}

@RequiresApi(Build.VERSION_CODES.O)
private fun ActivityEvent.subtitle(): String {
    val time = at?.let {
        DateUtils.getRelativeTimeSpanString(
            it.toEpochMilli(),
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS)
    }
    return listOfNotNull(byEmail, time).joinToString(" · ")
}