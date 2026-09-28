package com.example.smartmailbox.auth.ui.register

import android.content.MutableContextWrapper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartmailbox.R
import com.example.smartmailbox.auth.data.GoogleAuthHelper
import com.example.smartmailbox.ui.components.ErrorText
import com.example.smartmailbox.ui.theme.Alata
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.DarkGray
import com.example.smartmailbox.ui.theme.ErrorRed
import com.example.smartmailbox.ui.theme.VeryDarkGreen
import kotlinx.coroutines.launch

@Composable
fun RegisterView(
    registerViewModel: RegisterViewModel,
    paddingValues: PaddingValues,
    onBackToLogin: () -> Unit
) {
    val registerState = registerViewModel.registerState

    val context = LocalContext.current
    val contextWrapper = remember { MutableContextWrapper(context) }

    SideEffect {
        contextWrapper.baseContext = context
    }

    val googleAuthHelper = remember { GoogleAuthHelper(contextWrapper) }
    val coroutineScope = rememberCoroutineScope()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        Image(
            painter = painterResource(R.drawable.smart_mailbox_app_logo_transparent),
            contentDescription = "SmartMailbox logo",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 24.dp)
                .fillMaxWidth(0.8f),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Create account",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Create your SmartMailbox account.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = registerState.email,
                onValueChange = registerViewModel::onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                shape = RoundedCornerShape(5.dp),
                enabled = !registerState.isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Black,
                    unfocusedBorderColor = DarkGray,
                    focusedLabelColor = Black,
                    cursorColor = Black
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = registerState.password,
                onValueChange = registerViewModel::onPasswordChange,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                shape = RoundedCornerShape(5.dp),
                enabled = !registerState.isLoading,
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Black,
                    unfocusedBorderColor = DarkGray,
                    focusedLabelColor = Black,
                    cursorColor = Black
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = registerState.confirmPassword,
                onValueChange = registerViewModel::onConfirmPasswordChange,
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                shape = RoundedCornerShape(5.dp),
                enabled = !registerState.isLoading,
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Black,
                    unfocusedBorderColor = DarkGray,
                    focusedLabelColor = Black,
                    cursorColor = Black
                )
            )

            ErrorText(registerState.errorMessage)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    registerViewModel.register()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(0.dp, 8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                shape = RoundedCornerShape(5.dp),
                enabled = !registerState.isLoading
            ) {
                if (registerState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Register"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Black.copy(alpha = 0.7f)
                )

                Text(
                    text = "OR",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Black.copy(alpha = 0.7f)
                )

                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Black.copy(alpha = 0.7f)
                )
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        val idToken = googleAuthHelper.requestGoogleIdToken()
                        if (idToken != null) {
                            registerViewModel.registerWithGoogle(idToken)
                        } else {
                            // surface an error
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(5.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_google_logo),
                        contentDescription = "Google Logo",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterStart)
                    )

                    Text("Register with Google")
                }
            }

            TextButton(
                onClick = onBackToLogin,
                enabled = !registerState.isLoading
            ) {
                Text(
                    text = "Already have an account? Login.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}