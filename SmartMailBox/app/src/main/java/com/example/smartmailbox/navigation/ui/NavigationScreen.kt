package com.example.smartmailbox.navigation.ui

sealed class NavigationScreen(val route: String) {
    object Home : NavigationScreen("home")
    object Scan : NavigationScreen("scan")
    object Log : NavigationScreen("log")
    object Login : NavigationScreen("login")
    object Profile : NavigationScreen("profile")
    object FaceVerify : NavigationScreen("face_verify")
    object Register : NavigationScreen("register")
}