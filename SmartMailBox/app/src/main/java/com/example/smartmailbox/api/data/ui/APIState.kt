package com.example.smartmailbox.api.data.ui

import com.example.smartmailbox.api.data.PostMailBoxDataResponse

data class APIState(
    val response: PostMailBoxDataResponse? = null,
    val isLoading: Boolean = false,
    val isFinished: Boolean = false,
    val error: String? = null
)