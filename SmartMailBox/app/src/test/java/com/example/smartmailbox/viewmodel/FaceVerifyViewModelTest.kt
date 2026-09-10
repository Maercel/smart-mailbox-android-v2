package com.example.smartmailbox.viewmodel

import com.example.smartmailbox.auth.ui.faceverify.FaceVerifyViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class FaceVerifyViewModelTest {
    @Test
    fun stopCamera_setsCameraStartedFalse() {
        val viewModel = FaceVerifyViewModel()

        viewModel.startCamera()
        viewModel.stopCamera()

        assertFalse(viewModel.faceVerifyState.isCameraStarted)
    }

    @Test
    fun setErrorMessage_setsErrorAndStopsLoadingAndVerification() {
        val viewModel = FaceVerifyViewModel()

        viewModel.setErrorMessage("Face verification failed")

        assertEquals("Face verification failed", viewModel.faceVerifyState.errorMessage)
        assertFalse(viewModel.faceVerifyState.isLoading)
        assertFalse(viewModel.faceVerifyState.isVerified)
    }

    @Test
    fun clearVerificationFlag_setsVerifiedFalse() {
        val viewModel = FaceVerifyViewModel()

        viewModel.clearVerificationFlag()

        assertFalse(viewModel.faceVerifyState.isVerified)
    }
}