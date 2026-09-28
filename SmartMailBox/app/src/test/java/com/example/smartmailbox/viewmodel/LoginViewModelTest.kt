package com.example.smartmailbox.viewmodel

import com.example.smartmailbox.auth.ui.login.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class LoginViewModelTest {



    /*
    viewModel.login() requires FIREBASE so the tests fail.
    @Test
    fun login_setsErrorMessage_whenEmailAndPasswordAreEmpty() {
        val viewModel = LoginViewModel()

        viewModel.login()


        assertFalse(viewModel.loginState.errorMessage.isNullOrEmpty())
    }

    @Test
    fun login_setsErrorMessage_whenEmailIsEmpty() {
        val viewModel = LoginViewModel()

        viewModel.onPasswordChange("password123")
        viewModel.login()

        assertFalse(viewModel.loginState.errorMessage.isNullOrEmpty())
    }

    @Test
    fun login_setsErrorMessage_whenPasswordIsEmpty() {
        val viewModel = LoginViewModel()

        viewModel.onEmailChange("marcel")
        viewModel.login()

        assertFalse(viewModel.loginState.errorMessage.isNullOrEmpty())
    }

    @Test
    fun onUsernameChange_updatesEmailAndClearsError() {
        val viewModel = LoginViewModel()

        viewModel.login()
        viewModel.onEmailChange("marcel")

        assertEquals("marcel", viewModel.loginState.email)
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
    fun login_setsErrorMessage_whenEmailOnlyContainsSpaces() {
        val viewModel = LoginViewModel()

        viewModel.onEmailChange("   ")
        viewModel.onPasswordChange("password123")
        viewModel.login()

        assertFalse(viewModel.loginState.errorMessage.isNullOrEmpty())
    }
     */
}