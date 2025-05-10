package eu.tutorials.g1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import eu.tutorials.g1.model.*
import eu.tutorials.g1.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentScreen(navController: NavController) {
    var showSubscriptionDialog by remember { mutableStateOf(false) }
    var selectedTournament by remember { mutableStateOf<Tournament?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tournaments") },
                actions = {
                    IconButton(onClick = { /* TODO: Open filters */ }) {
                        Icon(Icons.Default.FilterList, "Filter")
                    }
                    IconButton(onClick = { 
                        // Check subscription before allowing tournament creation
                        if (hasTournamentCreationAccess()) {
                            navController.navigate(Screen.CreateTournament.route)
                        } else {
                            showSubscriptionDialog = true
                        }
                    }) {
                        Icon(Icons.Default.Add, "Create Tournament")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tournament List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sampleTournaments) { tournament ->
                    TournamentCard(
                        tournament = tournament,
                        onClick = { selectedTournament = tournament }
                    )
                }
            }
        }
    }

    // Subscription Dialog
    if (showSubscriptionDialog) {
        AlertDialog(
            onDismissRequest = { showSubscriptionDialog = false },
            title = { Text("Upgrade Required") },
            text = { 
                Column {
                    Text("Create tournaments with our subscription plans:")
                    Spacer(modifier = Modifier.height(16.dp))
                    SubscriptionPlanCard(SubscriptionPlan.BASIC)
                    Spacer(modifier = Modifier.height(8.dp))
                    SubscriptionPlanCard(SubscriptionPlan.PREMIUM)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { 
                        showSubscriptionDialog = false
                        navController.navigate(Screen.Subscription.route)
                    }
                ) {
                    Text("View Plans")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubscriptionDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Tournament Details Dialog
    selectedTournament?.let { tournament ->
        TournamentDetailsDialog(
            tournament = tournament,
            onDismiss = { selectedTournament = null },
            onJoin = { /* TODO: Handle team registration */ }
        )
    }
}

@Composable
fun TournamentCard(tournament: Tournament, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = tournament.name,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Teams: ${tournament.currentTeams}/${tournament.maxTeams}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Prize: ₹${tournament.prizePool}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Format: ${tournament.format}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Status: ${tournament.status}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun SubscriptionPlanCard(plan: SubscriptionPlan) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = plan.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "₹${plan.price}/month",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            plan.features.forEach { feature ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feature.name.replace("_", " "),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun TournamentDetailsDialog(
    tournament: Tournament,
    onDismiss: () -> Unit,
    onJoin: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(tournament.name) },
        text = {
            Column {
                Text(tournament.description)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Entry Fee: ₹${tournament.entryFee}")
                Text("Prize Pool: ₹${tournament.prizePool}")
                Text("Format: ${tournament.format}")
                Text("Teams: ${tournament.currentTeams}/${tournament.maxTeams}")
                Text("Registration Deadline: ${tournament.registrationDeadline}")
            }
        },
        confirmButton = {
            if (tournament.status == TournamentStatus.REGISTRATION_OPEN) {
                Button(onClick = onJoin) {
                    Text("Register Team")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

// Helper function to check subscription
private fun hasTournamentCreationAccess(): Boolean {
    // TODO: Implement actual subscription check
    return false
}

// Sample data
private val sampleTournaments = listOf(
    Tournament(
        id = "1",
        name = "Summer Football League",
        description = "Annual summer football tournament",
        maxTeams = 16,
        currentTeams = 8,
        prizePool = 50000.0,
        entryFee = 2000.0,
        format = TournamentFormat.GROUP_STAGE,
        status = TournamentStatus.REGISTRATION_OPEN
    ),
    Tournament(
        id = "2",
        name = "Basketball Championship",
        description = "City basketball championship",
        maxTeams = 8,
        currentTeams = 6,
        prizePool = 25000.0,
        entryFee = 1500.0,
        format = TournamentFormat.KNOCKOUT,
        status = TournamentStatus.REGISTRATION_OPEN
    )
) 