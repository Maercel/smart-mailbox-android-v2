package com.example.smartmailbox.auth.ui.faceverify

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.smartmailbox.ui.theme.Alata
import com.example.smartmailbox.ui.theme.ErrorRed
import com.example.smartmailbox.ui.theme.ForestGreen
import com.example.smartmailbox.ui.theme.VeryDarkGreen
import java.io.File

@Composable
fun FaceVerifyView(
    faceVerifyViewModel: FaceVerifyViewModel,
    paddingValues: PaddingValues,
    onVerifySuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val faceVerifyState = faceVerifyViewModel.faceVerifyState
    val context = LocalContext.current

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    LaunchedEffect(faceVerifyState.isVerified) {
        if (faceVerifyState.isVerified) {
            onVerifySuccess()
            faceVerifyViewModel.clearVerificationFlag()
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
            text = "Face Verification",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (faceVerifyState.isCameraStarted) {
                "Position your face in the camera and tap Verify Face."
            } else {
                "Tap Start Verification when you are ready."
            },
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (faceVerifyState.isCameraStarted) {
                FaceCameraPreview(
                    imageCapture = imageCapture,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        if (faceVerifyState.errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = faceVerifyState.errorMessage,
                fontFamily = Alata,
                fontSize = 14.sp,
                color = ErrorRed,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (!faceVerifyState.isCameraStarted) {
                    faceVerifyViewModel.startCamera()
                } else {
                    captureFaceImage(
                        context = context,
                        imageCapture = imageCapture,
                        onImageCaptured = { imageFile ->
                            faceVerifyViewModel.verifyFace(imageFile)
                        },
                        onError = { errorMessage ->
                            faceVerifyViewModel.setErrorMessage(errorMessage)
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ForestGreen,
                contentColor = VeryDarkGreen
            ),
            enabled = !faceVerifyState.isLoading
        ) {
            if (faceVerifyState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = if (faceVerifyState.isCameraStarted) {
                        "Verify Face"
                    } else {
                        "Start Verification"
                    },
                    fontFamily = Alata,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                faceVerifyViewModel.stopCamera()
                onBackToLogin()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ErrorRed,
                contentColor = VeryDarkGreen
            ),
            enabled = !faceVerifyState.isLoading
        ) {
            Text(
                text = "Back to Login",
                fontFamily = Alata,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun captureFaceImage(
    context: Context,
    imageCapture: ImageCapture,
    onImageCaptured: (File) -> Unit,
    onError: (String) -> Unit
) {
    val imageFile = File(
        context.cacheDir,
        "face_verify_${System.currentTimeMillis()}.jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(imageFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onImageCaptured(imageFile)
            }

            override fun onError(exception: ImageCaptureException) {
                onError(exception.message ?: "Failed to capture image")
            }
        }
    )
}