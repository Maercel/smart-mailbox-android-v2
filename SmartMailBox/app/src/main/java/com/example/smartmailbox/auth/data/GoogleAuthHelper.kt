package com.example.smartmailbox.auth.data

import android.content.MutableContextWrapper
import android.util.Base64
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.example.smartmailbox.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import java.security.SecureRandom

// Get context from the Composable rather than storing it in ViewModel, because ViewModel outlives
// rotation. Context in ViewModel wouldn't be updated.
// MutableContextWrapper so it swaps context on recreating (rotations etc.)
class GoogleAuthHelper(private val context: MutableContextWrapper) {

    // so it doesn't re-seed everytime we call the function
    companion object {
        private val secureRandom = SecureRandom()
    }
        suspend fun requestGoogleIdToken(): String? {
            val credentialManager = CredentialManager.create(context)
            val nonce = generateSecureRandomNonce()

            val option = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .setAutoSelectEnabled(true)
                .setNonce(nonce)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(option)
                .build()

            return try {
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    GoogleIdTokenCredential.createFrom(credential.data).idToken
                } else {
                    null
                }
            } catch (e: GetCredentialCancellationException) {
                // User closed the sheet
                Log.d("Credential Manager", "User cancelled sign-in")
                null


            } catch (e: GetCredentialException) {
                Log.e("Credential Manager", "Error requesting Google ID token ", e)
                null


            } catch (e: GoogleIdTokenParsingException) {   // parsing to GoogleId token failed
                Log.e("Credential Manager", "Error parsing Google ID token", e)
                null
            }
        }

    private fun generateSecureRandomNonce(byteLength: Int = 32): String {
        val randomBytes = ByteArray(byteLength)
        secureRandom.nextBytes(randomBytes)
        return Base64.encodeToString(randomBytes, Base64.NO_WRAP or Base64.URL_SAFE or Base64.NO_PADDING)
    }
}