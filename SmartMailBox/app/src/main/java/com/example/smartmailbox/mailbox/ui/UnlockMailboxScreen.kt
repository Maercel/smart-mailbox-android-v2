package com.example.smartmailbox.mailbox.ui

import android.graphics.Color.alpha
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartmailbox.R
import com.example.smartmailbox.mailbox.ui.components.PlayAgainButton
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.LightGray
import com.example.smartmailbox.ui.theme.White

@Composable
fun UnlockMailboxScreen(
    mailBoxViewModel: MailBoxViewModel,
    onBackButton: () -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    mailBoxViewModel.stopAudio()
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mailBoxViewModel.stopAudio()
        }
    }
    Scaffold(
        containerColor = White,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    onBackButton()
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = "Back arrow",
                    tint = Black
                    // no modifier needed, icon's default to 24.dp (Material spec)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                if (mailBoxViewModel.mailBoxState.isUnlockSoundPlaying) {
                    ExpandingRing(delay = 0)
                    ExpandingRing(delay = 500)
                    ExpandingRing(delay = 1000)
                    ExpandingRing(delay = 1500)
                    ExpandingRing(delay = 2000)
                    ExpandingRing(delay = 2500)
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize(0.8f)
                        .background(LightGray, CircleShape)
                        .border(2.dp, Black, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.lock_clock),
                        contentDescription = "Unlocking",
                        tint = Black,
                        modifier = Modifier.fillMaxSize(.5f)
                    )
                }
            }
            PlayAgainButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp),
                mailBoxViewModel = mailBoxViewModel,
                onClick = {
                    mailBoxViewModel.replayWavFile()
                }
            )
        }
    }
}

@Composable
fun ExpandingRing(delay: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "ring_transition")
    val ringSize = 200.dp
    val duration = 3000 // fixed for all, maybe change in the future

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 3.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = LinearEasing
            ),
            // shifts start time without altering loop speed
            initialStartOffset = StartOffset(delay),
            repeatMode = RepeatMode.Restart
        ),
        label = "scale_animation"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = LinearEasing
            ),
            initialStartOffset = StartOffset(delay), // Matches the scale offset perfectly
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha_animation"
    )

    Box(
        modifier = Modifier
            .size(ringSize)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .border(
                width = 4.dp,
                color = Black.copy(alpha = 0.9f),
                shape = CircleShape
            )
    )
}