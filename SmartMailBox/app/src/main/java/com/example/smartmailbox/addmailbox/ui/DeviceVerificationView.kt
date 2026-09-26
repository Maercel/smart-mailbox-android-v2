package com.example.smartmailbox.addmailbox.ui

import android.hardware.lights.Light
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.smartmailbox.addmailbox.domain.DeviceVerificationCodeFormat
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.LightGray

private const val CODE_GROUP_SIZE = 4

@Composable
fun DeviceVerificationView(
    modifier: Modifier = Modifier,
    verificationCode: String,
    onVerificationCodeChange: (String) -> Unit,
    enabled: Boolean = true,
    onDone: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }


        BasicTextField(
            value = verificationCode,
            onValueChange = onVerificationCodeChange,
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,                  // letters + digits
                capitalization = KeyboardCapitalization.None,       // case-sensitive codes
                autoCorrectEnabled = false,                         // or autoCorrect = false on older Compose
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onDone() }),
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            decorationBox = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    (0 until DeviceVerificationCodeFormat.LENGTH).chunked(CODE_GROUP_SIZE).forEachIndexed { groupIndex, group ->
                        if (groupIndex > 0) {
                            GroupSeparator(modifier = Modifier.align(Alignment.CenterVertically))
                        }
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            group.forEach { index ->
                                VerificationCodeBox(
                                    char = verificationCode.getOrNull(index),
                                    isActive = index == verificationCode.length,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        )
}

@Composable
private fun GroupSeparator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(15.dp)
            .height(5.dp)
            .background(Black, RoundedCornerShape(2.dp))
    )
}

@Composable
private fun VerificationCodeBox(char: Char?, isActive: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(0.75f)   // slightly taller than wide
            .border(
                width = 3.dp,
                color = if (isActive) Black else LightGray,
                shape = RoundedCornerShape(5.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char?.toString().orEmpty(),
            style = MaterialTheme.typography.titleMedium
        )
    }
}