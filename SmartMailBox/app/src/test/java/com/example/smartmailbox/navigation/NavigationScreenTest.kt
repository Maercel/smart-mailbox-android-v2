package com.example.smartmailbox.navigation

import com.example.smartmailbox.navigation.ui.NavigationScreen
import org.junit.Assert
import org.junit.Test

class NavigationScreenTest {

    @Test
    fun navigationRoutes_haveExpectedValues() {
        Assert.assertEquals("home", NavigationScreen.Home.route)
        Assert.assertEquals("scan", NavigationScreen.Scan.route)
        Assert.assertEquals("log", NavigationScreen.Log.route)
        Assert.assertEquals("login", NavigationScreen.Login.route)
        Assert.assertEquals("face_verify", NavigationScreen.FaceVerify.route)
        Assert.assertEquals("register", NavigationScreen.Register.route)
    }
}