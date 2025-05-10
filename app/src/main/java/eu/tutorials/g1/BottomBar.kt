package eu.tutorials.g1

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem(
            title = Screen.MyTeams.title.toString(),
            icon = Screen.MyTeams.icon ?: Icons.Default.Group,
            route = Screen.MyTeams.route
        ),
        BottomNavItem(
            title = Screen.RecentChats.title.toString(),
            icon = Screen.RecentChats.icon ?: Icons.Default.Chat,
            route = Screen.RecentChats.route
        ),
        BottomNavItem(
            title = Screen.Tournaments.title.toString(),
            icon = Screen.Tournaments.icon ?: Icons.Default.EmojiEvents,
            route = Screen.Tournaments.route
        ),
        BottomNavItem(
            title = Screen.Discover.title.toString(),
            icon = Screen.Discover.icon ?: Icons.Default.Explore,
            route = Screen.Discover.route
        ),
        BottomNavItem(
            title = Screen.Profile.title.toString(),
            icon = Screen.Profile.icon ?: Icons.Default.Person,
            route = Screen.Profile.route
        )
    )

    NavigationBar(
        containerColor = Color(0xFF1A237E),
        contentColor = Color.White,
        modifier = Modifier.height(64.dp)
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color(0xFF0D47A1)
                )
            )
        }
    }
}

data class BottomNavItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)
