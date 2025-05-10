package eu.tutorials.g1

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String, 
    val title: String? = null, 
    val showBottomBar: Boolean = false,
    val icon: ImageVector? = null
) {
    // Auth Screens
    object Login : Screen("login", "Login", false)
    object Register : Screen("register", "Sign Up", false)

    // Sport Selection
    object SportSelection : Screen("sport_selection", "Select Sport", true)

    // Team Management
    object CreateOrJoin : Screen("create_or_join", "Create or Join Team", true) {
        fun createRoute(sport: String) = "$route/$sport"
    }
    object CreateTeam : Screen("create_team", "Create Team", true) {
        fun createRoute(sport: String) = "$route/$sport"
    }
    object JoinTeam : Screen("join_team", "Join Team", true) {
        fun createRoute(sport: String) = "$route/$sport"
    }
    object MyTeams : Screen("my_teams", "My Teams", true, Icons.Default.Group)

    // Chat
    object ChatRoom : Screen("chat_room", "Chat Room", false) {
        fun createRoute(teamId: String) = "$route/$teamId"
    }
    object RecentChats : Screen("recent_chats", "Recent Chats", true, Icons.Default.Chat)

    // Profile
    object Profile : Screen("profile", "Profile", true, Icons.Default.Person)
    object EditProfile : Screen("edit_profile", "Edit Profile", false)

    // Tournaments
    object Tournaments : Screen("tournaments", "Tournaments", true, Icons.Default.EmojiEvents)
    object CreateTournament : Screen("create_tournament", "Create Tournament", false)
    object TournamentDetails : Screen("tournament_details", "Tournament Details", false) {
        fun createRoute(tournamentId: String) = "$route/$tournamentId"
    }
    object TournamentRegistration : Screen("tournament_registration", "Tournament Registration", false) {
        fun createRoute(tournamentId: String) = "$route/$tournamentId"
    }
    object Subscription : Screen("subscription", "Subscription Plans", false)

    // Discover
    object Discover : Screen("discover", "Discover", true, Icons.Default.Explore)

    companion object {
        fun fromRoute(route: String?): Screen {
            return when (route) {
                Login.route -> Login
                Register.route -> Register
                SportSelection.route -> SportSelection
                CreateOrJoin.route -> CreateOrJoin
                CreateTeam.route -> CreateTeam
                JoinTeam.route -> JoinTeam
                MyTeams.route -> MyTeams
                RecentChats.route -> RecentChats
                Profile.route -> Profile
                EditProfile.route -> EditProfile
                Tournaments.route -> Tournaments
                CreateTournament.route -> CreateTournament
                Subscription.route -> Subscription
                Discover.route -> Discover
                else -> Login
            }
        }

        fun createRoute(baseRoute: String, vararg args: String): String {
            return buildString {
                append(baseRoute)
                args.forEach { arg ->
                    append("/$arg")
                }
            }
        }
    }
}
