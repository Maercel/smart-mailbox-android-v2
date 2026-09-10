package com.example.smartmailbox.scanner.ui

data class ScannerState(
    val scannedCode: String = "",
    val isScannerRunning: Boolean = false,
)