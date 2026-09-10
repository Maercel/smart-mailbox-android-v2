package com.example.smartmailbox.viewmodel

import com.example.smartmailbox.auth.ui.login.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class LoginViewModelTest {

    @Test
    fun login_setsErrorMessage_whenUsernameAndPasswordAreEmpty() {
        val viewModel = LoginViewModel()

        viewModel.login()

        assertEquals(
            "Username and password are required",
            viewModel.loginState.errorMessage
        )
        assertFalse(viewModel.loginState.isLoading)
    }

    @Test
    fun login_setsErrorMessage_whenUsernameIsEmpty() {
        val viewModel = LoginViewModel()

        viewModel.onPasswordChange("password123")
        viewModel.login()

        assertEquals(
            "Username and password are required",
            viewModel.loginState.errorMessage
        )
        assertFalse(viewModel.loginState.isLoading)
    }

    @Test
    fun login_setsErrorMessage_whenPasswordIsEmpty() {
        val viewModel = LoginViewModel()

        viewModel.onUsernameChange("marcel")
        viewModel.login()

        assertEquals(
            "Username and password are required",
            viewModel.loginState.errorMessage
        )
        assertFalse(viewModel.loginState.isLoading)
    }

    @Test
    fun onUsernameChange_updatesUsernameAndClearsError() {
        val viewModel = LoginViewModel()

        viewModel.login()
        viewModel.onUsernameChange("marcel")

        assertEquals("marcel", viewModel.loginState.username)
        assertNull(viewModel.loginState.errorMessage)
    }

    @Test
    fun onPasswordChange_updatesPasswordAndClearsError() {
        val viewModel = LoginViewModel()

        viewModel.login()
        viewModel.onPasswordChange("password123")

        assertEquals("password123", viewModel.loginState.password)
        assertNull(viewModel.loginState.errorMessage)
    }

    @Test
    fun clearLoginNavigationFlags_resetsLoginAndTwoFactorFlags() {
        val viewModel = LoginViewModel()

        viewModel.clearLoginNavigationFlags()

        assertFalse(viewModel.loginState.isLoggedIn)
        assertFalse(viewModel.loginState.twoFactorRequired)
    }

    @Test
    fun login_setsErrorMessage_whenUsernameOnlyContainsSpaces() {
        val viewModel = LoginViewModel()

        viewModel.onUsernameChange("   ")
        viewModel.onPasswordChange("password123")
        viewModel.login()

        assertEquals(
            "Username and password are required",
            viewModel.loginState.errorMessage
        )
        assertFalse(viewModel.loginState.isLoading)
    }
}