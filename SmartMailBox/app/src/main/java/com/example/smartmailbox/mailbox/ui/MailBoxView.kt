package com.example.smartmailbox.mailbox.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartmailbox.mailbox.ui.components.OpenMailBoxButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.smartmailbox.R
import com.example.smartmailbox.ui.theme.Alata
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.ErrorRed
import com.example.smartmailbox.ui.theme.LightGray

@Composable
fun MailBoxView(
    mailBoxViewModel: MailBoxViewModel = viewModel(),
    paddingValues: PaddingValues,
    onBackButton: () -> Unit,
    onOpenMailbox: () -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mailBoxViewModel.startScanner()
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
        QRCodeScannerView(
            onQrCodeScanned = { mailBoxViewModel.onQrCodeScanned(it) }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()

                    val scanSize = 280.dp.toPx()
                    val left = (size.width - scanSize) / 2
                    val top = (size.height - scanSize) / 2

                    // Dark tint
                    drawRect(
                        color = Black.copy(alpha = 0.7f)
                    )

                    // Clear cutout window
                    drawRect(
                        color = Color.Transparent,
                        topLeft = Offset(left, top),
                        size = Size(scanSize, scanSize),
                        blendMode = BlendMode.Clear,
                    )
                }
        )

        IconButton(
            onClick = onBackButton,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_back),
                contentDescription = stringResource(R.string.back_button),
                tint = LightGray,
                modifier = Modifier.size(28.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .offset(y = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.qr_code_scanner),
                contentDescription = "QR code scanner",
                tint = LightGray,
                modifier = Modifier.size(48.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Scan the QR code",
                    style = MaterialTheme.typography.titleLarge,
                    color = LightGray
                )
                Text(
                    text = "on a Direct4.me box",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightGray.copy(alpha = 0.8f)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (mailBoxViewModel.scannerState.scannedCode.isNotEmpty()
                    && mailBoxViewModel.mailBoxState.error == null) {
                    OpenMailBoxButton(
                        mailBoxViewModel = mailBoxViewModel,
                        onClick = {
                            mailBoxViewModel.openMailbox(
                                context.cacheDir,
                                onOpenMailbox
                            )
                        }
                    )
                }
                else {
                    mailBoxViewModel.mailBoxState.error?.let { error ->
                        Text(
                            text = error,
                            fontFamily = Alata,
                            fontSize = 18.sp,
                            color = ErrorRed,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}