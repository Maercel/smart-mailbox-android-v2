package com.example.smartmailbox.api.data


/*
data class PostMailBoxData(
    val deliveryId: Int,
    val boxId: Int,
    val tokenFormat: Int,
    val latitude: Double,
    val longitude: Double,
    val qrCodeInfo: String,
    val terminalSeed: Int,
    val isMultibox: Boolean,
    val doorIndex: Int,
    val addAccessLog: Boolean,
    val confirmation: Boolean,
    val userId: Int
)
*/

data class PostMailBoxData(
    val boxId: Int,
    val tokenFormat: Int
)