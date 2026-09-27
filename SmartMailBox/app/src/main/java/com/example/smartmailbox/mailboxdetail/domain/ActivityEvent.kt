package com.example.smartmailbox.mailboxdetail.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.smartmailbox.mailboxdetail.data.ActivityDto
import java.time.Instant

data class ActivityEvent(
    val id: String,
    val type: ActivityType,
    val byEmail: String?,   // who did it, null for automatic/not-user events
    val at: Instant?,
)

@RequiresApi(Build.VERSION_CODES.O)
fun ActivityDto.toDomain(): ActivityEvent? {
    val type = ActivityType.entries.find { it.name == type } ?: return null   // check the type, unknown type: skip
    return ActivityEvent(
        id = id,
        type = type,
        byEmail = byEmail,
        at = at?.toDate()?.toInstant(), // January 1970, 00:00 UTC is Instant, the same as Timestamp
    )
}