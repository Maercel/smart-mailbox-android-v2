package com.example.smartmailbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.smartmailbox.ui.theme.Black

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit,
    onInboxClick: () -> Unit
) {
    val appTitle = stringResource(R.string.app_name)

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(8.dp)
                .height(64.dp),
        ) {
            IconButton(
                onClick = { onInboxClick() },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(R.drawable.inbox_icon),
                    contentDescription = "Inbox",
                    tint = Black,
                    modifier = Modifier
                        .size(28.dp)
                    // icon's default is 24.dp
                )
            }

            Image(
                painter = painterResource(R.drawable.smart_mailbox_app_logo_transparent),
                contentDescription = "Smart Mailbox logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxHeight(0.225f), // leave some breathing room with fraction (0.8f)
                contentScale = ContentScale.Fit
            )

            IconButton(
                onClick = onProfileClick,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
            ) {
                Icon(
                    painter = painterResource(R.drawable.account_box),
                    contentDescription = "Profile",
                    tint = Black,
                    modifier = Modifier
                        .size(28.dp)
                )
            }
        }
    }
}