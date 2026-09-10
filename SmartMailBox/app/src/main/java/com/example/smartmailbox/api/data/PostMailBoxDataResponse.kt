package com.example.smartmailbox.api.data

data class PostMailBoxDataResponse(
    val result: Int,
    val message: String?,
    val validationErrors: Map<String, String>?,
    val errorNumber: Int,
    val data: String?
)
