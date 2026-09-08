package com.example.smartmailbox.view

import android.content.MutableContextWrapper
import android.util.Log
import androidx.compose.ui.graphics.Color
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartmailbox.R
import com.example.smartmailbox.auth.GoogleAuthHelper
import com.example.smartmailbox.ui.theme.Alata
import com.example.smartmailbox.ui.theme.ErrorRed
import com.example.smartmailbox.ui.theme.VeryDarkGreen
import com.example.smartmailbox.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginView(
    loginViewModel: LoginViewModel,
    onTwoFactorRequired: () -> Unit,
    onRegisterClick: () -> Unit
) {

    val loginState = loginViewModel.loginState

    val context = LocalContext.current
    val contextWrapper = remember { MutableContextWrapper(context) }

    SideEffect {
        contextWrapper.baseContext = context
    }

    val googleAuthHelper = remember { GoogleAuthHelper(contextWrapper) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = loginState.email,
            onValueChange = loginViewModel::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,
            shape = RoundedCornerShape(5.dp),
            enabled = !loginState.isLoading,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VeryDarkGreen,
                unfocusedBorderColor = VeryDarkGreen,
                focusedLabelColor = VeryDarkGreen,
                cursorColor = VeryDarkGreen
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = loginState.password,
            onValueChange = loginViewModel::onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,
            enabled = !loginState.isLoading,
            shape = RoundedCornerShape(5.dp),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VeryDarkGreen,
                unfocusedBorderColor = VeryDarkGreen,
                focusedLabelColor = VeryDarkGreen,
                cursorColor = VeryDarkGreen
            )
        )

        if (loginState.errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = loginState.errorMessage,
                fontFamily = Alata,
                fontSize = 14.sp,
                color = ErrorRed,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { loginViewModel.login() },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(0.dp, 8.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(5.dp),
            enabled = !loginState.isLoading
        ) {
            if (loginState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Login")
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
                color = VeryDarkGreen.copy(alpha = 0.7f)

            )

            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 12.dp),
                style = MaterialTheme.typography.bodySmall,
                color = VeryDarkGreen.copy(alpha = 0.7f)
            )

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = VeryDarkGreen.copy(alpha = 0.7f)
            )
        }


        Button(
            onClick = {
                coroutineScope.launch {
                    val idToken = googleAuthHelper.requestGoogleIdToken()
                    if (idToken != null) {
                        loginViewModel.loginWithGoogle(idToken)
                    } else {
                        Log.e("LoginView", "Google ID token request failed")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
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

                Text("Login with Google")
            }
        }

        //Spacer(modifier = Modifier.height(5.dp))

        TextButton(
            onClick = onRegisterClick,
            enabled = !loginState.isLoading
        ) {
            Text(
                text = "Don't have an account? Register",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
