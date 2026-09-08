package com.example.smartmailbox.domain.model

data class ScannerState(
    val scannedCode: String = "",
    val isScannerRunning: Boolean = false,
)
