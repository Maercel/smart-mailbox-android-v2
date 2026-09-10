package com.example.smartmailbox

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartmailbox.navigation.domain.NavigationItem
import com.example.smartmailbox.navigation.ui.NavigationScreen
import com.example.smartmailbox.ui.theme.DarkGreen
import com.example.smartmailbox.ui.theme.ForestGreen
import com.example.smartmailbox.ui.theme.LightMint

@Composable
fun AppFooter(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    // last screen in stack (the one displaying) AsState, compose is watching it
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        NavigationItem(
            title = "Home",
            selectedIcon = R.drawable.home_icon,
            route = NavigationScreen.Home.route
        ),
        NavigationItem(
            title = "Scan",
            selectedIcon = R.drawable.qr_code_scanner,
            route = NavigationScreen.Scan.route
        ),
        NavigationItem(
            title = "Log",
            selectedIcon = R.drawable.docs_icon,
            route = NavigationScreen.Log.route
        )
    )

    /*
    Box(
        modifier = modifier
        .fillMaxWidth()
        .background(DarkGreen)
        .padding(20.dp)
        .height(48.dp),
        contentAlignment = Alignment.Center,

    ) {

    }
    */
    NavigationBar(
        containerColor = DarkGreen,
        contentColor = LightMint
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute == NavigationScreen.Profile.route) {
                        navController.popBackStack() // rough patch for profile screen
                    }
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true // pause and save
                        }
                        launchSingleTop = true // prevent duplicates
                        restoreState = true // restore previous saved state of screen
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.selectedIcon),
                        contentDescription = item.title
                    )
                },

                label = {
                    Text(
                        item.title,
                        color = LocalContentColor.current
                    )
                },

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LightMint,
                    unselectedIconColor = LightMint.copy(alpha = 0.7f),
                    selectedTextColor = LightMint,
                    unselectedTextColor = LightMint.copy(alpha = 0.7f),
                    indicatorColor = ForestGreen.copy(alpha = 0.3f)
                )
            )
        }
    }
}