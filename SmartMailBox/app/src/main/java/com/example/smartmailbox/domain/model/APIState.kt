package com.example.smartmailbox.domain.model

import com.example.smartmailbox.api.PostMailBoxDataResponse

data class APIState(
    val response: PostMailBoxDataResponse? = null,
    val isLoading: Boolean = false,
    val isFinished: Boolean = false,
    val error: String? = null
)
