package com.example.smartmailbox.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.api.AuthRetrofitInstance
import com.example.smartmailbox.domain.model.FaceVerifyState
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File

class FaceVerifyViewModel : ViewModel() {

    var faceVerifyState by mutableStateOf(FaceVerifyState())
        private set

    fun verifyFace(imageFile: File) {
        viewModelScope.launch {
            faceVerifyState = faceVerifyState.copy(
                isLoading = true,
                errorMessage = null,
                isVerified = false
            )

            try {
                val requestBody = imageFile.asRequestBody(
                    "image/jpeg".toMediaTypeOrNull()
                )

                val currentImagePart = MultipartBody.Part.createFormData(
                    name = "current_image",
                    filename = imageFile.name,
                    body = requestBody
                )

                val response = AuthRetrofitInstance.api.postFaceVerify(
                    currentImage = currentImagePart
                )

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body?.verified == true) {
                        faceVerifyState = faceVerifyState.copy(
                            isLoading = false,
                            isVerified = true,
                            errorMessage = null
                        )
                    } else {
                        faceVerifyState = faceVerifyState.copy(
                            isLoading = false,
                            isVerified = false,
                            errorMessage = body?.message ?: "Face verification failed"
                        )
                    }
                } else {
                    faceVerifyState = faceVerifyState.copy(
                        isLoading = false,
                        isVerified = false,
                        errorMessage = getErrorMessage(response.errorBody()?.string())
                    )
                }
            } catch (e: Exception) {
                faceVerifyState = faceVerifyState.copy(
                    isLoading = false,
                    isVerified = false,
                    errorMessage = e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun startCamera() {
        faceVerifyState = faceVerifyState.copy(
            isCameraStarted = true,
            errorMessage = null
        )
    }

    fun stopCamera() {
        faceVerifyState = faceVerifyState.copy(
            isCameraStarted = false
        )
    }

    fun clearVerificationFlag() {
        faceVerifyState = faceVerifyState.copy(
            isVerified = false
        )
    }

    fun setErrorMessage(message: String) {
        faceVerifyState = faceVerifyState.copy(
            isLoading = false,
            errorMessage = message,
            isVerified = false
        )
    }

    private fun getErrorMessage(errorJson: String?): String {
        if (errorJson.isNullOrBlank()) {
            return "Face verification failed"
        }

        return try {
            JSONObject(errorJson).optString("message", "Face verification failed")
        } catch (e: Exception) {
            "Face verification failed"
        }
    }
}