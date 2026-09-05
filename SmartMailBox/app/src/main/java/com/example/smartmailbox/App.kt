package com.example.smartmailbox

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartmailbox.auth.AuthRepository
import com.example.smartmailbox.navigation.NavigationScreen
import com.example.smartmailbox.ui.theme.SmartMailBoxTheme
import com.example.smartmailbox.view.FaceVerifyView
import com.example.smartmailbox.view.HomeView
import com.example.smartmailbox.view.LogView
import com.example.smartmailbox.view.LoginView
import com.example.smartmailbox.view.MailBoxView
import com.example.smartmailbox.view.ProfileView
import com.example.smartmailbox.view.RegisterView
import com.example.smartmailbox.viewmodel.FaceVerifyViewModel
import com.example.smartmailbox.viewmodel.HomeViewModel
import com.example.smartmailbox.viewmodel.LogViewModel
import com.example.smartmailbox.viewmodel.LoginViewModel
import com.example.smartmailbox.viewmodel.MailBoxViewModel
import com.example.smartmailbox.viewmodel.ProfileViewModel
import com.example.smartmailbox.viewmodel.RegisterViewModel


@Composable
fun App() {
    /* doesn't survive recomposition (screen rotation)
    var mailboxViewModel by remember {
        mutableStateOf(MailboxViewModel())
    }
    */

    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = viewModel()
    val mailBoxViewModel: MailBoxViewModel = viewModel()
    val logModel: LogViewModel = viewModel()
    val loginModel: LoginViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val faceVerifyViewModel: FaceVerifyViewModel = viewModel()
    val registerViewModel: RegisterViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showMainBars = currentRoute !in listOf(
        NavigationScreen.Login.route,
        NavigationScreen.FaceVerify.route
    )


    SmartMailBoxTheme {
        Scaffold(
            topBar = {
                if (showMainBars) {
                    TopAppBar(
                        onProfileClick = {
                            navController.navigate(NavigationScreen.Profile.route) {
                                // so we don't stack profile screens like Home -> Profile -> Profile -> Profile
                                launchSingleTop = true
                            }
                        }
                    )
                }
            },
            bottomBar = {
                if (showMainBars) {
                    AppFooter(navController = navController)
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = NavigationScreen.Login.route,
                enterTransition = { fadeIn(tween(200)) },
                exitTransition = { fadeOut(tween(200)) },
                popEnterTransition = { fadeIn(tween(200)) },
                popExitTransition = { fadeOut(tween(200)) }
            ) {
                // navigateBack exists
                composable(NavigationScreen.Login.route) {
                    LoginView(
                        loginViewModel = loginModel,
                        onLoginSuccess = {
                            navController.navigate(NavigationScreen.Home.route) {
                                popUpTo(NavigationScreen.Login.route) {
                                    inclusive = true // So backstack becomes Home (it removes Login)
                                }
                            }
                        },
                        onTwoFactorRequired = {
                            navController.navigate(NavigationScreen.FaceVerify.route)
                        },
                        onRegisterClick = {
                            navController.navigate(NavigationScreen.Register.route)
                        }
                    )
                }
                composable(NavigationScreen.Profile.route) {
                    ProfileView(
                        profileViewModel = profileViewModel,
                        paddingValues = paddingValues,
                        onTwoFactorClick = {
                            // later navigate to Enable2FA screen
                        }
                    )
                }
                composable(NavigationScreen.FaceVerify.route) {
                    FaceVerifyView(
                        faceVerifyViewModel = faceVerifyViewModel,
                        paddingValues = paddingValues,
                        onVerifySuccess = {
                            navController.navigate(NavigationScreen.Home.route) {
                                popUpTo(NavigationScreen.Login.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onBackToLogin = {
                            navController.popBackStack(
                                route = NavigationScreen.Login.route,
                                inclusive = false
                            )
                        }
                    )
                }
                composable(NavigationScreen.Register.route) {
                    RegisterView(
                        registerViewModel = registerViewModel,
                        paddingValues = paddingValues,
                        onRegisterSuccess = {
                            navController.navigate(NavigationScreen.Home.route) {
                                popUpTo(NavigationScreen.Register.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onBackToLogin = {
                            navController.popBackStack(
                                route = NavigationScreen.Login.route,
                                inclusive = false
                            )
                        }
                    )
                }
                composable(NavigationScreen.Home.route) { HomeView(homeViewModel, paddingValues) }
                composable(NavigationScreen.Scan.route) { MailBoxView(mailBoxViewModel, paddingValues) }
                composable(NavigationScreen.Log.route) { LogView(logModel, paddingValues) }
            }
            // MailBoxView(mailBoxViewModel, paddingValues)
            // HomeView(paddingValues = paddingValues)
            // LogView(paddingValues = paddingValues)
        }
    }
}
