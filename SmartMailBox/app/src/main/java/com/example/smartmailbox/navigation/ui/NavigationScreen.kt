package com.example.smartmailbox.navigation.ui

sealed class NavigationScreen(val route: String) {
    object Home : NavigationScreen("home")
    object Scan : NavigationScreen("scan")
    object Activity : NavigationScreen("activity")
    object Login : NavigationScreen("login")
    object Profile : NavigationScreen("profile")
    object FaceVerify : NavigationScreen("face_verify")
    object Register : NavigationScreen("register")
    object UnlockMailbox : NavigationScreen("unlock_mailbox")
    object AddMailbox : NavigationScreen("add_mailbox")
    object Inbox : NavigationScreen("inbox")

    object MailboxDetail : NavigationScreen("mailbox_detail/{mailboxId}") {
        const val ARG_MAILBOX_ID = "mailboxId"
        fun createRoute(mailboxId: String) = "mailbox_detail/$mailboxId"
    }

    object MailboxActivity : NavigationScreen("mailbox_activity/{mailboxId}") {
        const val ARG_MAILBOX_ID = "mailboxId"
        fun createRoute(mailboxId: String) = "mailbox_activity/$mailboxId"
    }

    object MailboxSettings : NavigationScreen("mailbox_settings/{mailboxId}") {
        const val ARG_MAILBOX_ID = "mailboxId"
        fun createRoute(mailboxId: String) = "mailbox_settings/$mailboxId"
    }

    object MailboxAccess : NavigationScreen("mailbox_access/{mailboxId}") {
        const val ARG_MAILBOX_ID = "mailboxId"
        fun createRoute(mailboxId: String) = "mailbox_access/$mailboxId"
    }

    object MailboxHelp : NavigationScreen("mailbox_help/{mailboxId}") {
        const val ARG_MAILBOX_ID = "mailboxId"
        fun createRoute(mailboxId: String) = "mailbox_help/$mailboxId"
    }

}