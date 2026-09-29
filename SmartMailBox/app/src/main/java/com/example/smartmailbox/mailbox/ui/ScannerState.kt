package com.example.smartmailbox.mailbox.ui

data class ScannerState(
    val scannedCode: String = "",
    val isScannerRunning: Boolean = false
)