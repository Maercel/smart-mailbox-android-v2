package com.example.smartmailbox.auth

import android.util.Log
import android.util.Log.e
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    val isLoggedIn: Boolean
        get() = firebaseAuth.currentUser != null

    suspend fun registerAccount(
        username: String,
        email: String,
        password: String
    ) : Boolean {
        return try {
            val result = suspendCancellableCoroutine<Boolean> { continuation ->
                val task = firebaseAuth.createUserWithEmailAndPassword(email, password)
                task.addOnSuccessListener { authResult  ->
                    if (continuation.isActive) {
                        val uid = authResult .user!!.uid
                        val usernameData  = hashMapOf(
                            "uid" to uid,
                            "email" to email
                        )

                        firestore.collection("usernames")
                            .document(username)
                            .set(usernameData)
                            .addOnSuccessListener { documentReference ->
                                Log.d("Firestore", "Username saved successfully!")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Error adding username!", e)
                            }

                        scope.launch {
                            login(email, password)
                            continuation.resume(true)
                        }
                    }
                }

                task.addOnFailureListener {
                    if (continuation.isActive) {
                        continuation.resume(false)
                    }
                }

                continuation.invokeOnCancellation {
                    // clean up / cancel Firebase operation
                }
            }

            result
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            false
        }
    }


    suspend fun login(
        username: String,
        password: String
    ): Boolean {
        return try {
            val userEmail = resolveEmail(username)
            if (userEmail.isNullOrBlank()) return false

            val result = suspendCancellableCoroutine { continuation ->

                val task = firebaseAuth.signInWithEmailAndPassword(userEmail, password)

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
                    // clean up / cancel Firebase operation
                }
            }
            result
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            false
        }
    }

    fun logout() = firebaseAuth.signOut()

    private suspend fun resolveEmail(identifier: String): String? {
        if (identifier.contains("@")) {
            return identifier
        }

        return suspendCancellableCoroutine { continuation ->
            firestore.collection("usernames")
                .document(identifier)
                .get()
                .addOnSuccessListener { document ->
                    val email = document.getString("email")
                    if (continuation.isActive) {
                        continuation.resume(email)
                    }
                }
                .addOnFailureListener {
                    if (continuation.isActive) {
                        continuation.resume(null)
                    }
                }

            continuation.invokeOnCancellation {
                // clean up / cancel Firebase operation
            }
        }
    }

}

