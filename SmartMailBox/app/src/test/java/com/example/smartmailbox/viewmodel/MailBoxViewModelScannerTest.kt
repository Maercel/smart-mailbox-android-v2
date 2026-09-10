package com.example.smartmailbox.viewmodel

import com.example.smartmailbox.mailbox.ui.MailBoxViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MailBoxViewModelScannerTest {

    @Test
    fun startScanner_setsScannerRunningTrue() {
        val viewModel = MailBoxViewModel()

        viewModel.startScanner()

        assertTrue(viewModel.scannerState.isScannerRunning)
    }

    @Test
    fun stopScanner_setsScannerRunningFalse() {
        val viewModel = MailBoxViewModel()

        viewModel.startScanner()
        viewModel.stopScanner()

        assertFalse(viewModel.scannerState.isScannerRunning)
    }
}