package com.example.smartmailbox.auth

import android.content.MutableContextWrapper
import android.util.Base64
import android.util.Log
import android.util.Log.e
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.example.smartmailbox.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.security.SecureRandom
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    val isLoggedIn: Boolean
        get() = firebaseAuth.currentUser != null

    val authStateFlow: Flow<Boolean?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    suspend fun registerAccount(
        email: String,
        password: String
    ) : Boolean {
        return try {
            val result = suspendCancellableCoroutine<Boolean> { continuation ->
                val task = firebaseAuth.createUserWithEmailAndPassword(email, password)
                task.addOnSuccessListener { authResult  ->
                    if (continuation.isActive) {
                        /*
                        scope.launch {
                            login(email, password)
                            continuation.resume(true)
                        }
                        */
                        continuation.resume(true)
                    }
                }

                task.addOnFailureListener {
                    if (continuation.isActive) {
                        continuation.resume(false)
                    }
                }

                continuation.invokeOnCancellation {
                    // once firebase operation has been fired, you can't cancel it
                    // so this is here for proper memory management

                }
            }

            result
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseAuthException) {
            Log.e(TAG, "Firebase authentication failed", e)
            false
        } catch (e: Exception) {
            // cancellationException is derived from Exception so you would need to throw it here to
            // if (e is CancellationException) throw e
            false
        }
    }


    suspend fun login(
        email: String,
        password: String
    ): Boolean {
        return try {
            val result = suspendCancellableCoroutine { continuation ->

                val task = firebaseAuth.signInWithEmailAndPassword(email, password)

                task.addOnSuccessListener {
                    if (continuation.isActive) {
                        continuation.resume(true)
                    }
                }

                task.addOnFailureListener {
                    if (continuation.isActive) {
                        continuation.resume(false)
                    }
                }

                continuation.invokeOnCancellation {
                    // once firebase operation has been fired, you can't cancel it
                    // so this is here for proper memory management
                }
            }
            result
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseAuthException) {
            Log.e(TAG, "Firebase authentication failed", e)
            false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun signInWithGoogleIdToken(idToken: String): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
            true
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseAuthException) {
            Log.e(TAG, "Firebase authentication failed", e)
            false
        } catch (e: Exception) {
            false
        }
    }

    // deletes local user's authentication tokens and data from device's disk cache
    // instead of signing out via network request
    fun logout() = firebaseAuth.signOut()

    companion object {
        private const val TAG = "AuthRepository"
    }
}

