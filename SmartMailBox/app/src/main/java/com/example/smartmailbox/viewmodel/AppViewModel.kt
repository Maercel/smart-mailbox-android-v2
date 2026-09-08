package com.example.smartmailbox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.auth.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// make authRepository dependency-injected
class AppViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    val isLoggedIn: StateFlow<Boolean?> = authRepository.authStateFlow
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null)
    // 5000 for 5 seconds without UI, then it will stop observing/collecting
}