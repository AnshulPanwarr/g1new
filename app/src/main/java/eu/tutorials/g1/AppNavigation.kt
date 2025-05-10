package eu.tutorials.g1

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import eu.tutorials.g1.Screen.Discover
import eu.tutorials.g1.Screen.Login
import eu.tutorials.g1.ui.theme.G1Theme
import eu.tutorials.g1.BottomBar
import eu.tutorials.g1.LoginScreen
import eu.tutorials.g1.SignUpScreen
import eu.tutorials.g1.GameSport
import eu.tutorials.g1.CreateOrJoinScreen
import eu.tutorials.g1.CreateTeam
import eu.tutorials.g1.JoinTeam
import eu.tutorials.g1.MyTeams
import eu.tutorials.g1.ChatRoom
import eu.tutorials.g1.RecentChats
import eu.tutorials.g1.Profile
import eu.tutorials.g1.EditProfile
import eu.tutorials.g1.Tournaments
import eu.tutorials.g1.TournamentDetails
import eu.tutorials.g1.TournamentRegistration
import eu.tutorials.g1.DiscoverScreen
import eu.tutorials.g1.CreateTournament
import eu.tutorials.g1.SubscriptionScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(navController: NavHostController) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen = currentDestination?.route?.let { route ->
        Screen.fromRoute(route)
    }

    G1Theme {
        Scaffold(
            topBar = {
                if (currentScreen?.title != null) {
                    TopAppBar(
                        title = { Text(currentScreen.title) },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            },
            bottomBar = {
                // Show bottom bar for main screens
                if (currentScreen?.showBottomBar == true || 
                    currentScreen?.route == Screen.MyTeams.route ||
                    currentScreen?.route == Screen.RecentChats.route ||
                    currentScreen?.route == Screen.Tournaments.route ||
                    currentScreen?.route == Screen.Discover.route ||
                    currentScreen?.route == Screen.Profile.route) {
                    BottomBar(navController = navController)
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Login.route
                ) {
                    // Auth Screens
                    composable(Screen.Login.route) {
                        LoginScreen(navController = navController)
                    }
                    composable(Screen.Register.route) {
                        SignUpScreen(navController = navController)
                    }

                    // Sport Selection
                    composable(Screen.SportSelection.route) {
                        GameSport(navController = navController)
                    }

                    // Team Management
                    composable(
                        route = "${Screen.CreateOrJoin.route}/{sport}",
                        arguments = listOf(navArgument("sport") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val sport = backStackEntry.arguments?.getString("sport") ?: ""
                        CreateOrJoinScreen(
                            navController = navController,
                            selectedSport = sport,
                            sport = sport
                        )
                    }
                    composable(
                        route = "${Screen.CreateTeam.route}/{sport}",
                        arguments = listOf(navArgument("sport") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val sport = backStackEntry.arguments?.getString("sport") ?: ""
                        CreateTeam(
                            navController = navController,
                            selectedSport = sport,
                            sport = sport
                        )
                    }
                    composable(
                        route = "${Screen.JoinTeam.route}/{sport}",
                        arguments = listOf(navArgument("sport") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val sport = backStackEntry.arguments?.getString("sport") ?: ""
                        JoinTeam(navController = navController, sport1 = sport)
                    }
                    composable(Screen.MyTeams.route) {
                        MyTeams(navController = navController)
                    }

                    // Chat
                    composable(
                        route = "${Screen.ChatRoom.route}/{teamId}",
                        arguments = listOf(navArgument("teamId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val teamId = backStackEntry.arguments?.getString("teamId") ?: ""
                        ChatRoom(navController = navController, teamId = teamId)
                    }
                    composable(Screen.RecentChats.route) {
                        RecentChats(navController = navController)
                    }

                    // Profile
                    composable(Screen.Profile.route) {
                        Profile(navController = navController)
                    }
                    composable(Screen.EditProfile.route) {
                        EditProfile(navController = navController)
                    }

                    // Tournaments
                    composable(Screen.Tournaments.route) {
                        Tournaments(navController = navController)
                    }
                    composable(Screen.CreateTournament.route) {
                        CreateTournament(navController = navController)
                    }
                    composable(
                        route = "${Screen.TournamentDetails.route}/{tournamentId}",
                        arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val tournamentId = backStackEntry.arguments?.getString("tournamentId") ?: ""
                        TournamentDetails(navController = navController, tournamentId = tournamentId)
                    }
                    composable(
                        route = "${Screen.TournamentRegistration.route}/{tournamentId}",
                        arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val tournamentId = backStackEntry.arguments?.getString("tournamentId") ?: ""
                        TournamentRegistration(navController = navController, tournamentId = tournamentId)
                    }

                    // Subscription
                    composable(Screen.Subscription.route) {
                        SubscriptionScreen(navController = navController)
                    }

                    // Discover
                    composable(Screen.Discover.route) {
                        DiscoverScreen(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun Discover(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Discover Screen - Coming Soon")
    }
}

@Composable
fun TournamentDetails(navController: NavHostController, tournamentId: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Tournament Details Screen - Coming Soon")
    }
}

@Composable
fun TournamentRegistration(navController: NavHostController, tournamentId: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Tournament Registration Screen - Coming Soon")
    }
}

@Composable
fun EditProfile(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Edit Profile Screen - Coming Soon")
    }
}

@Composable
fun CreateTournament(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Create Tournament Screen - Coming Soon")
    }
}

@Composable
fun SubscriptionScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Subscription Screen - Coming Soon")
    }
}
