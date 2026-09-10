package com.example.smartmailbox.profile.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.auth.domain.AuthRepository
import kotlinx.coroutines.launch
import org.json.JSONObject


class ProfileViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    var profileState by mutableStateOf(ProfileState())
        private set

    fun loadProfile() {
        viewModelScope.launch {
            profileState = profileState.copy(
                isLoading = true,
                errorMessage = null
            )
            /*
            try {
                val response = AuthRetrofitInstance.api.getProfile()

                if (response.isSuccessful) {
                    val body = response.body()

                    profileState = profileState.copy(
                        username = body?.username ?: "",
                        email = body?.email ?: "",
                        twoFactorEnabled = body?.twoFactorEnabled ?: false,
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    profileState = profileState.copy(
                        isLoading = false,
                        errorMessage = getErrorMessage(response.errorBody()?.string())
                    )
                }
            } catch (e: Exception) {
                profileState = profileState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Something went wrong"
                )
            }*/
        }
    }

    private fun getErrorMessage(errorJson: String?): String {
        if (errorJson.isNullOrBlank()) {
            return "Failed to load profile"
        }

        return try {
            JSONObject(errorJson).optString("message", "Failed to load profile")
        } catch (e: Exception) {
            "Failed to load profile"
        }
    }

    fun logout() {
        profileState = profileState.copy(
            isLoading = true,
            errorMessage = null
        )
        val success = authRepository.logout()

        profileState = ProfileState()
    }
}