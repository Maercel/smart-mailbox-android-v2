package com.example.smartmailbox.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmailbox.api.PostMailBoxData
import com.example.smartmailbox.api.RetrofitInstance
import com.example.smartmailbox.domain.model.APIState
import com.example.smartmailbox.domain.model.MailBoxState
import com.example.smartmailbox.domain.model.ScannerState
import kotlinx.coroutines.launch
import android.util.Base64
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream
import java.io.ByteArrayInputStream
import android.media.MediaPlayer
import com.example.smartmailbox.util.MailBoxQRParser

class MailBoxViewModel : ViewModel() {
    var scannerState by mutableStateOf(ScannerState())
        private set

    var mailBoxState by mutableStateOf(MailBoxState())
        private set

    var apiState by mutableStateOf(APIState())
        private set

    private var mediaPlayer: MediaPlayer? = null

    fun onQrCodeScanned(code: String) {
        /*
        Safety check. Camera can capture even after I call cancelScanner()
        Camera runs on background thread and proccess 30-60FPS
        Camera overrode my scannedCode because camera ran
        after cancelScanner()
        */
        if (scannerState.isScannerRunning) {
            scannerState = scannerState.copy(scannedCode = code)
        }
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

    private fun createPostMailBoxData(url: String) : PostMailBoxData = PostMailBoxData(
        boxId =  MailBoxQRParser.extractMailBoxId(url),
        tokenFormat = 2 // wavzip
    )

    private fun decodeBase64ToBytes(base64String: String): ByteArray {
        return Base64.decode(base64String, Base64.DEFAULT)
    }

    private fun playWavFile(wavFile: File) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(wavFile.absolutePath)
            prepare()
            start()
            setOnCompletionListener {
                Log.d("MailBoxAPI", "Predvajanje končano")
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

    fun openMailbox(outputDir: File) {
        viewModelScope.launch {
            apiState = apiState.copy(isLoading = true)
            try {
                val data = createPostMailBoxData(scannerState.scannedCode)
                // call API
                val response = RetrofitInstance.api.postMailBoxData(
                    data
                )

                apiState = apiState.copy(response = response.body())
                //mailBoxState = MailBoxState(isMailBoxOpen = true)
                Log.d("MailBoxAPI", "Response: $response")
                Log.d("MailBoxAPI", "Response object: ${response.body()}")

                val base64Data = response.body()?.data
                if (base64Data != null) {
                    val zipBytes = decodeBase64ToBytes(base64Data)
                    val wavFile = extractWavFromZip(zipBytes, outputDir)
                    Log.d("MailBoxAPI", "WAV file ready at: ${wavFile?.absolutePath}")
                    if (wavFile != null) {
                        playWavFile(wavFile)
                    } else {
                        Log.e("MailBoxAPI", "WAV datoteka ni bila najdena v zipu")
                    }
                } else {
                    Log.e("MailBoxAPI", "data je null v odgovoru")
                }
                apiState = apiState.copy(isLoading = false)
            } catch(e: Exception) {
                apiState = APIState(isLoading = false, error = e.toString())
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}