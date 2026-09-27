package com.example.smartmailbox.profile.ui

import android.media.midi.MidiDeviceInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.auth.domain.AuthRepository
import com.example.smartmailbox.profile.domain.UserProfile
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.internal.throwMissingFieldException
import org.json.JSONObject


class ProfileViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()



    init {
        val profile = authRepository.currentUserProfile()
        if (profile != null) {
            _profileState.value = ProfileState(
                email = profile.email,
                hasPassword = profile.hasPassword,
            )
        }
    }

    fun logout() {
        authRepository.logout()

        //TODO: Update app.kt so viewmodel doesn't live in activity but in composable then delete this line
        _profileState.value = ProfileState()
    }
}