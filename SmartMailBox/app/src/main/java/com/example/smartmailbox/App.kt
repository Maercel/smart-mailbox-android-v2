package com.example.smartmailbox

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartmailbox.addmailbox.ui.AddMailboxView
import com.example.smartmailbox.addmailbox.ui.AddMailboxViewModel
import com.example.smartmailbox.addmailbox.ui.DeviceVerificationView
import com.example.smartmailbox.navigation.ui.NavigationScreen
import com.example.smartmailbox.ui.theme.SmartMailBoxTheme
import com.example.smartmailbox.auth.ui.faceverify.FaceVerifyView
import com.example.smartmailbox.home.ui.HomeView
import com.example.smartmailbox.log.ui.LogView
import com.example.smartmailbox.auth.ui.login.LoginView
import com.example.smartmailbox.mailbox.ui.MailBoxView
import com.example.smartmailbox.profile.ui.ProfileView
import com.example.smartmailbox.auth.ui.register.RegisterView
import com.example.smartmailbox.auth.ui.faceverify.FaceVerifyViewModel
import com.example.smartmailbox.home.ui.HomeViewModel
import com.example.smartmailbox.log.ui.LogViewModel
import com.example.smartmailbox.auth.ui.login.LoginViewModel
import com.example.smartmailbox.mailbox.ui.MailBoxViewModel
import com.example.smartmailbox.profile.ui.ProfileViewModel
import com.example.smartmailbox.auth.ui.register.RegisterViewModel
import com.example.smartmailbox.inbox.ui.InboxView
import com.example.smartmailbox.mailbox.ui.UnlockMailboxScreen
import com.example.smartmailbox.mailboxactivity.ui.MailboxActivityView
import com.example.smartmailbox.mailboxactivity.ui.MailboxActivityViewModel
import com.example.smartmailbox.mailboxdetail.ui.MailboxDetailView
import com.example.smartmailbox.mailboxdetail.ui.MailboxDetailViewModel


@RequiresApi(Build.VERSION_CODES.O)
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
    //val profileViewModel: ProfileViewModel = viewModel() //TODO: Moved to composable, do the same with others, then delete this comment
    val faceVerifyViewModel: FaceVerifyViewModel = viewModel()
    val registerViewModel: RegisterViewModel = viewModel()
    val appViewModel: AppViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showMainBars = currentRoute !in listOf(
        NavigationScreen.Login.route,
        NavigationScreen.Register.route,
        NavigationScreen.FaceVerify.route,
        NavigationScreen.Scan.route,
        NavigationScreen.UnlockMailbox.route
    )

    val isLoggedIn by appViewModel.isLoggedIn.collectAsStateWithLifecycle()

    // launches every time isLoggedIn changes: login, logout
    LaunchedEffect(isLoggedIn) {
        when (isLoggedIn) {
            true -> {
                if (currentRoute != NavigationScreen.Home.route) {
                    navController.navigate(NavigationScreen.Home.route) {
                        // Clear everything up to and including the start destination
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
            false -> {
                if (currentRoute != NavigationScreen.Login.route &&
                    currentRoute != NavigationScreen.Register.route &&
                    currentRoute != NavigationScreen.FaceVerify.route
                ) {
                    navController.navigate(NavigationScreen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            null -> { /* do nothing still initializing */ }
        }
    }



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
                        },
                        onInboxClick = {
                            navController.navigate(NavigationScreen.Inbox.route) {
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
                startDestination = NavigationScreen.Home.route,
                enterTransition = { fadeIn(tween(200)) },
                exitTransition = { fadeOut(tween(200)) },
                popEnterTransition = { fadeIn(tween(200)) },
                popExitTransition = { fadeOut(tween(200)) }
            ) {
                // navigateBack exists
                composable(NavigationScreen.Login.route) {
                    LoginView(
                        loginViewModel = loginModel,
                        onTwoFactorRequired = {
                            navController.navigate(NavigationScreen.FaceVerify.route)
                        },
                        onRegisterClick = {
                            navController.navigate(NavigationScreen.Register.route)
                        }
                    )
                }
                composable(NavigationScreen.Profile.route) {
                    val profileViewModel: ProfileViewModel = viewModel()

                    ProfileView(
                        profileViewModel = profileViewModel,
                        paddingValues = paddingValues,
                        onBack = { navController.popBackStack() },
                        onEditProfileClick = { },            // TODO: edit profile screen
                        onChangePasswordClick = { },         // TODO: change password screen
                        onNotificationSettingsClick = { },   // TODO: notification settings screen
                    )
                }
                composable(NavigationScreen.FaceVerify.route) {
                    FaceVerifyView(
                        faceVerifyViewModel = faceVerifyViewModel,
                        paddingValues = paddingValues,
                        onVerifySuccess = {
                            navController.navigate(NavigationScreen.Home.route) {
                                popUpTo(NavigationScreen.Home.route) { inclusive = true }
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
                        onBackToLogin = {
                            navController.popBackStack(
                                route = NavigationScreen.Login.route,
                                inclusive = false
                            )
                        }
                    )
                }
                composable(NavigationScreen.Home.route) {
                    HomeView(
                        homeViewModel,
                        paddingValues,
                        navigateToAddMailboxScreen = {
                            navController.navigate(NavigationScreen.AddMailbox.route) {
                            }
                        },
                        onMailboxClick = { mailboxId ->
                            navController.navigate(NavigationScreen.MailboxDetail.createRoute(mailboxId))
                        }
                    )

                }
                composable(NavigationScreen.Scan.route) {
                    MailBoxView(
                        mailBoxViewModel,
                        paddingValues,
                        onBackButton = { navController.popBackStack() },
                        onOpenMailbox = {
                            navController.navigate(NavigationScreen.UnlockMailbox.route)
                        }
                    )
                }
                composable(NavigationScreen.UnlockMailbox.route) {
                    UnlockMailboxScreen(
                        mailBoxViewModel,
                        onBackButton = { navController.popBackStack() }
                    )
                }
                composable(NavigationScreen.AddMailbox.route) {
                    val addMailboxViewModel: AddMailboxViewModel = viewModel()

                    AddMailboxView(
                        addMailboxViewModel,
                        paddingValues,
                        onBackButton = {
                            navController.popBackStack()
                        },
                        onMailboxAdded = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(NavigationScreen.Inbox.route) {
                    InboxView(
                        paddingValues,
                        onBackButton = { navController.popBackStack() }
                    )
               }

                composable(
                    route = NavigationScreen.MailboxDetail.route,
                    arguments = listOf(
                        // Handles address shaped like this: mailbox_detail/{something(mailboxId)}
                        navArgument(NavigationScreen.MailboxDetail.ARG_MAILBOX_ID) { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val mailboxId = backStackEntry.arguments
                        ?.getString(NavigationScreen.MailboxDetail.ARG_MAILBOX_ID)
                        ?: return@composable

                    // viewModel attaches to the nearest owner (in this case here), so in activity (where I had it before, top of the app)
                    // it lives until the app closes.
                    // one instance per opened mailbox
                    val mailboxDetailViewModel: MailboxDetailViewModel = viewModel()

                    MailboxDetailView(
                        mailboxDetailViewModel = mailboxDetailViewModel,
                        paddingValues = paddingValues,
                        onBack = { navController.popBackStack() },
                        onSettingsClick = { },
                        onActivityClick = {
                            navController.navigate(NavigationScreen.MailboxActivity.createRoute(mailboxId))
                        },
                        onAccessClick = { },
                        onHelpClick = { }
                    )
                }

                composable(
                    route = NavigationScreen.MailboxActivity.route,
                    arguments = listOf(
                        navArgument(NavigationScreen.MailboxActivity.ARG_MAILBOX_ID) { type = NavType.StringType }
                    )
                ) {
                    val mailboxActivityViewModel: MailboxActivityViewModel = viewModel()

                    MailboxActivityView(
                        mailboxActivityViewModel = mailboxActivityViewModel,
                        paddingValues = paddingValues,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(NavigationScreen.Log.route) { LogView(logModel, paddingValues) }
            }
            // MailBoxView(mailBoxViewModel, paddingValues)
            // HomeView(paddingValues = paddingValues)
            // LogView(paddingValues = paddingValues)
        }
    }

    fun navigateToHomeAndClearBackStack() {
        navController.navigate(NavigationScreen.Home.route) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    fun navigateToLoginAndClearBackStack() {
        navController.navigate(NavigationScreen.Login.route) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
}
