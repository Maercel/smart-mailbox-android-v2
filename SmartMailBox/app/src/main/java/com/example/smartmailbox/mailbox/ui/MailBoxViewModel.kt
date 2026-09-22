package com.example.smartmailbox.mailbox.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.api.data.remote.PostMailBoxData
import com.example.smartmailbox.api.data.remote.RetrofitInstance
import com.example.smartmailbox.api.data.ui.APIState
import com.example.smartmailbox.scanner.ui.ScannerState
import kotlinx.coroutines.launch
import android.util.Base64
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream
import java.io.ByteArrayInputStream
import android.media.MediaPlayer
import com.example.smartmailbox.scanner.domain.MailBoxQRParser
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Duration.Companion.milliseconds

class MailBoxViewModel : ViewModel() {
    var scannerState by mutableStateOf(ScannerState())
        private set

    var mailBoxState by mutableStateOf(MailBoxState())
        private set

    var apiState by mutableStateOf(APIState())
        private set

    private var mediaPlayer: MediaPlayer? = null
    private var lastWavFile: File? = null

    fun onQrCodeScanned(code: String) {
        /*
        Safety check. Camera can capture even after I call cancelScanner()
        Camera runs on background thread and proccess 30-60FPS
        Camera overrode my scannedCode because camera ran
        after cancelScanner()
        */

        scannerState = scannerState.copy(scannedCode = code)

    }

    fun startScanner() {
        scannerState = ScannerState(isScannerRunning = true)
    }

    /*
    fun cancelScanner() {
        scannerState = ScannerState()
    }
    */


    fun stopScanner() {
        scannerState = scannerState.copy(isScannerRunning = false)
    }

    private suspend fun displayErrorMessageAndPause(message: String) {
        stopScanner()
        mailBoxState = mailBoxState.copy(error = message)

        delay(2_000.milliseconds)

        mailBoxState = mailBoxState.copy(error = null)
        startScanner()
    }

    private fun createPostMailBoxData(url: String) : PostMailBoxData = PostMailBoxData(
        boxId =  MailBoxQRParser.extractMailBoxId(url),
        tokenFormat = 2 // wavzip
    )

    private fun decodeBase64ToBytes(base64String: String): ByteArray {
        return Base64.decode(base64String, Base64.DEFAULT)
    }

    private fun playWavFile(wavFile: File) {
        mediaPlayer?.release()
        lastWavFile = wavFile

        mediaPlayer = MediaPlayer().apply {
            setDataSource(wavFile.absolutePath)
            prepare()

            mailBoxState = mailBoxState.copy(isUnlockSoundPlaying = true)
            start()
            setOnCompletionListener {
                Log.d("MailBoxAPI", "Predvajanje končano")

                mailBoxState = mailBoxState.copy(isUnlockSoundPlaying = false)

                it.release()
                mediaPlayer = null
            }
        }
    }

    private fun extractWavFromZip(zipBytes: ByteArray, outputDir: File): File? {
        val zipInputStream = ZipInputStream(ByteArrayInputStream(zipBytes))
        var wavFile: File? = null

        var entry = zipInputStream.nextEntry
        while (entry != null) {
            if (entry.name.endsWith(".wav")) {
                val outFile = File(outputDir, entry.name)
                FileOutputStream(outFile).use { fos ->
                    zipInputStream.copyTo(fos)
                }
                wavFile = outFile
                Log.d("MailBoxAPI", "Extracted WAV: ${outFile.absolutePath}")
            }
            zipInputStream.closeEntry()
            entry = zipInputStream.nextEntry
        }

        zipInputStream.close()
        return wavFile
    }

    fun openMailbox(
        outputDir: File,
        navigateToUnlockMailBoxScreen: () -> Unit
    ) {
        viewModelScope.launch {
            apiState = apiState.copy(isLoading = true)
            val data = try {
                createPostMailBoxData(scannerState.scannedCode).also {
                    validatePostMailBoxData(it)
                }
            } catch (e: IllegalArgumentException) {
                Log.e("MailBoxAPI", "Invalid mailbox QR code\"", e)

                scannerState = ScannerState()
                apiState = apiState.copy(isLoading = false)
                displayErrorMessageAndPause("Invalid mailbox QR code")

                return@launch
            }
            // call API
            try {
                val response = RetrofitInstance.api.postMailBoxData(data)

                apiState = apiState.copy(
                    response = response.body(),
                    isLoading = false
                )

                Log.d("MailBoxAPI", "Response: $response")
                Log.d("MailBoxAPI", "Response object: ${response.body()}")

                if (!response.isSuccessful) {
                    displayErrorMessageAndPause("Error occurred, try later")
                    return@launch
                }

                val base64Data = response.body()?.data

                if (base64Data == null) {
                    Log.e("MailBoxAPI", "data is null in response")
                    displayErrorMessageAndPause("Error occurred, try later")
                    return@launch
                }

                val zipBytes = decodeBase64ToBytes(base64Data)
                val wavFile = extractWavFromZip(zipBytes, outputDir)

                Log.d(
                    "MailBoxAPI",
                    "WAV file ready at: ${wavFile?.absolutePath}"
                )

                if (wavFile == null) {
                    Log.e("MailBoxAPI", "WAV file not found in ZIP")
                    displayErrorMessageAndPause("Error occurred, try later")
                    return@launch
                }

                playWavFile(wavFile)

                scannerState = ScannerState()
                navigateToUnlockMailBoxScreen()

            } catch (e: Exception) {
                Log.e("MailBoxAPI", "Failed to open mailbox", e)

                apiState = apiState.copy(
                    isLoading = false,
                    error = "Failed to open mailbox"
                )

                displayErrorMessageAndPause("Error occurred, try later")
            }
        }
    }

    private fun validatePostMailBoxData(data: PostMailBoxData) {
        if (data.boxId <= 0) {
            throw IllegalArgumentException("boxId must be a positive integer")
        }
        if (data.tokenFormat !in 0..6) {
            throw IllegalArgumentException("Invalid token format")
        }
    }

    fun replayWavFile() {
        lastWavFile?.let { playWavFile(it) }
    }

    fun stopAudio() {
        mediaPlayer?.let { player ->
            if (player.isPlaying)
                player.stop()

            player.release()
        }
        mediaPlayer = null
        mailBoxState = mailBoxState.copy(isUnlockSoundPlaying = false)
    }

    override fun onCleared() {
        super.onCleared()
        stopAudio()
    }

}