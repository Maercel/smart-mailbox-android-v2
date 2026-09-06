package com.example.smartmailbox.view

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartmailbox.R
import com.example.smartmailbox.ui.theme.Alata
import com.example.smartmailbox.ui.theme.ErrorRed
import com.example.smartmailbox.ui.theme.ForestGreen
import com.example.smartmailbox.ui.theme.VeryDarkGreen
import com.example.smartmailbox.viewmodel.RegisterViewModel

@Composable
fun RegisterView(
    registerViewModel: RegisterViewModel,
    paddingValues: PaddingValues,
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val registerState = registerViewModel.registerState

    LaunchedEffect(registerState.isRegistered) {
        if (registerState.isRegistered) {
            onRegisterSuccess()
            registerViewModel.clearRegisterNavigationFlag()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Register",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))
        /*
        OutlinedTextField(
            value = registerState.username,
            onValueChange = registerViewModel::onUsernameChange,
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,
            shape = RoundedCornerShape(5.dp),
            enabled = !registerState.isLoading,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VeryDarkGreen,
                unfocusedBorderColor = VeryDarkGreen,
                focusedLabelColor = VeryDarkGreen,
                cursorColor = VeryDarkGreen
            )
        )

        Spacer(modifier = Modifier.height(12.dp))
        */
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
                focusedBorderColor = VeryDarkGreen,
                unfocusedBorderColor = VeryDarkGreen,
                focusedLabelColor = VeryDarkGreen,
                cursorColor = VeryDarkGreen
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
                focusedBorderColor = VeryDarkGreen,
                unfocusedBorderColor = VeryDarkGreen,
                focusedLabelColor = VeryDarkGreen,
                cursorColor = VeryDarkGreen
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
                focusedBorderColor = VeryDarkGreen,
                unfocusedBorderColor = VeryDarkGreen,
                focusedLabelColor = VeryDarkGreen,
                cursorColor = VeryDarkGreen
            )
        )

        if (registerState.errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = registerState.errorMessage,
                fontFamily = Alata,
                fontSize = 14.sp,
                color = ErrorRed,
                fontWeight = FontWeight.Bold
            )
        }

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
                color = VeryDarkGreen
            )

            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 12.dp),
                style = MaterialTheme.typography.bodySmall
            )

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = VeryDarkGreen
            )
        }


        Button(
            onClick = { registerViewModel.registerWithGoogle() },
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

                Text("Register with Google")
            }
        }

        //Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onBackToLogin,
            enabled = !registerState.isLoading
        ) {
            Text(
                text = "Already have an account? Login",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}