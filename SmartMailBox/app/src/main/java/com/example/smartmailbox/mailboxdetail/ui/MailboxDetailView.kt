package com.example.smartmailbox.mailboxdetail.ui

import android.graphics.Color
import android.hardware.lights.Light
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartmailbox.R
import com.example.smartmailbox.mailboxdetail.domain.LockState
import com.example.smartmailbox.ui.components.ScreenHeader
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.ErrorRed
import com.example.smartmailbox.ui.theme.LightGray
import com.example.smartmailbox.ui.theme.White

//TODO: NEEDS REMODEL!
@Composable
fun MailboxDetailView(
    mailboxDetailViewModel: MailboxDetailViewModel,
    paddingValues: PaddingValues,
    onBack: () -> Unit,
    onSettingsClick: () -> Unit,
    onActivityClick: () -> Unit,
    onAccessClick: () -> Unit,
    onHelpClick: () -> Unit,
    onUnlockClick: () -> Unit,
    onLockdownClick: () -> Unit,
) {
    val mailboxViewModelState by mailboxDetailViewModel.mailboxDetailState.collectAsStateWithLifecycle()

    val tiles = listOf(
        DetailTile("Activity", R.drawable.ic_history, onActivityClick),
        DetailTile("Access", R.drawable.ic_key, onAccessClick),
        DetailTile(
            label = "Lockdown",
            iconRes = R.drawable.locked,
            onClick = { },                                              // TODO: lockdown dialog
        ),
        DetailTile("Help", R.drawable.ic_product_support, onHelpClick),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        ScreenHeader(
            title = mailboxViewModelState.name,
            onBack = onBack,
            actions = {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = "Settings",
                        tint = Black
                    )
                }
            }
        )

        HorizontalDivider(
            color = Black.copy(alpha = 0.1f)
        )

        when {
            mailboxViewModelState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(56.dp),
                        color = LightGray,
                        strokeWidth = 5.dp
                    )
                }
            }

            mailboxViewModelState.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mailboxViewModelState.errorMessage.orEmpty(),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(mailboxViewModelState.imageRes),
                        contentDescription = mailboxViewModelState.name,
                        modifier = Modifier
                            .fillMaxSize(0.5f)
                            .aspectRatio(1f),
                        contentScale = Fit
                    )

                    FloorShadow(
                        modifier = Modifier
                            .fillMaxWidth(0.55f)
                            .height(28.dp)

                    )

                    Spacer(Modifier.height(24.dp))
                    MailboxStatusRow(
                        batteryPercent = mailboxViewModelState.batteryPercent,
                        isConnected = mailboxViewModelState.isConnected
                    )

                    Spacer(Modifier.height(24.dp))

                    if (mailboxViewModelState.lockState == LockState.OPEN) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_mailbox_door),
                                contentDescription = null,  // null, otherwise accessibility service announces "Mailbox door", I think
                                tint = Black,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Door open. Locks automatically when closed",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    UnlockButton(
                        isLocked = mailboxViewModelState.lockState == LockState.LOCKED,
                        isLoading = mailboxViewModelState.isLockPending,
                        enabled = mailboxViewModelState.isConnected &&
                            mailboxViewModelState.lockState != LockState.OPEN,
                        onClick = mailboxDetailViewModel::onLockButtonClick
                    )


                    Spacer(Modifier.height(24.dp))
                    DetailTileGrid(tiles)
                }
            }
        }
    }
}

@Composable
private fun MailboxStatusRow(batteryPercent: Int, isConnected: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(56.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_battery_android_full),
                contentDescription = "Battery percentage",
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text("${batteryPercent}%")
        }
        Text(
            text = if (isConnected) "Connected" else "Offline",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


private data class DetailTile(
    val label: String,
    @DrawableRes val iconRes: Int,
    val onClick: () -> Unit,
)

@Composable
private fun UnlockButton(
    isLocked: Boolean,
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val containerColor = if (isLocked)
        Black.copy(alpha = 0.8f).compositeOver(White)
    else LightGray

    val contentColor = if (isLocked) White else Black

    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor.copy(alpha = 0.4f),
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = contentColor, // important! without that we wouldn't see it switching between one of the colors
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Icon(
                painter = painterResource(if (isLocked) R.drawable.locked else R.drawable.ic_unlocked_right),
                contentDescription = if (isLocked) "Unlock mailbox" else "Lock mailbox",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
@Composable
private fun DetailTileGrid(tiles: List<DetailTile>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)           // same gap as horizontal
    ) {
        tiles.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                row.forEach { tile ->
                    DetailTileCard(tile, Modifier.weight(1f))
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun DetailTileCard(tile: DetailTile, modifier: Modifier = Modifier) {
    Card(
        onClick = tile.onClick,
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightGray,
            contentColor = Black
        ),
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(tile.iconRes),
                contentDescription = null,
                tint = Black,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = tile.label,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun FloorShadow(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val radius = size.height / 2
        scale(scaleX = size.width / size.height, scaleY = 1f) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Black.copy(alpha = 0.55f), Black.copy(alpha = 0f)),
                    center = center,
                    radius = radius
                ),
                radius = radius,
                center = center
            )
        }
    }
}