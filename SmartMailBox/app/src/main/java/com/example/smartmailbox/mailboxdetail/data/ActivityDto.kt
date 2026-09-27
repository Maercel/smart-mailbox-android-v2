package com.example.smartmailbox.mailboxdetail.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class ActivityDto(
    @DocumentId val id: String = "",
    val type: String = "",
    val byEmail: String? = null,
    val at: Timestamp? = null,
)