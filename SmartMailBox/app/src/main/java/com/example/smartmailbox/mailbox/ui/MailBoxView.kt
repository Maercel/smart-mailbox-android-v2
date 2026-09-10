package com.example.smartmailbox.mailbox.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartmailbox.mailbox.ui.components.OpenMailBoxButton
import com.example.smartmailbox.scanner.ui.components.ScanQRCodeButton
import androidx.compose.ui.platform.LocalContext
import com.example.smartmailbox.scanner.ui.QRCodeScannerView

@Composable
fun MailBoxView(mailBoxViewModel: MailBoxViewModel = viewModel(), paddingValues: PaddingValues) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                //Lifecycle.Event.ON_RESUME -> mailBoxViewModel.startScanner()
                Lifecycle.Event.ON_PAUSE -> mailBoxViewModel.stopScanner()
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mailBoxViewModel.stopScanner()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
        if (mailBoxViewModel.scannerState.isScannerRunning) {

            QRCodeScannerView(
                onQrCodeScanned = { mailBoxViewModel.onQrCodeScanned(it) }
            )

            Button(
                onClick = { mailBoxViewModel.stopScanner() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .height(48.dp)
                    .padding(5.dp),
                shape = RoundedCornerShape(5.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
            ) {
                Text("Cancel")
            }

            if (mailBoxViewModel.scannerState.scannedCode.isNotEmpty()) {
                ScanQRCodeButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    onClick = { mailBoxViewModel.stopScanner() }
                )
            }

        } else {
            if (mailBoxViewModel.scannerState.scannedCode.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Mailbox code:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        mailBoxViewModel.scannerState.scannedCode,
                        style = MaterialTheme.typography.bodySmall
                    )

                    OpenMailBoxButton(
                        mailBoxViewModel = mailBoxViewModel,
                        onClick = { mailBoxViewModel.openMailbox(context.cacheDir) })
                }
            }

            ScanQRCodeButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                onClick = {
                    mailBoxViewModel.startScanner()
                }
            )
        }
    }
}