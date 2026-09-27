package com.example.smartmailbox.mailboxactivity.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.home.data.FirestoreMailboxRepository
import com.example.smartmailbox.navigation.ui.NavigationScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@RequiresApi(Build.VERSION_CODES.O)
class MailboxActivityViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val mailboxId: String =
        checkNotNull(savedStateHandle[NavigationScreen.MailboxActivity.ARG_MAILBOX_ID])

    private val repository = FirestoreMailboxRepository(
        FirebaseFirestore.getInstance(),
        FirebaseAuth.getInstance()
    )

    private val _activityState = MutableStateFlow(MailboxActivityState())
    val activityState: StateFlow<MailboxActivityState> = _activityState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeActivity(mailboxId)
                .catch { e ->
                    Log.e("MailboxActivity", "observeActivity failed for $mailboxId", e)
                    _activityState.update {
                        it.copy(isLoading = false, errorMessage = "Couldn't load activity.")
                    }
                }
                .collect { events ->
                    _activityState.update {
                        it.copy(isLoading = false, errorMessage = null, events = events)
                    }
                }
        }
    }

    /*
    // making data THE state
    @RequiresApi(Build.VERSION_CODES.O)
    val activityState: StateFlow<MailboxActivityState> =
        repository.observeActivity(mailboxId)
            .map { events -> MailboxActivityState(isLoading = false, events = events) }
            .catch { e ->
                Log.e("MailboxActivity", "observeActivity failed for $mailboxId", e)
                emit(MailboxActivityState(isLoading = false, errorMessage = "Couldn't load activity."))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000.milliseconds),
                initialValue = MailboxActivityState(), // spin until data arrives
            )
     */
}