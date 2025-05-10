package eu.tutorials.g1

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import eu.tutorials.g1.model.Team

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrJoinScreen(navController: NavHostController, selectedSport: String, sport: String) {
    var teams by remember { mutableStateOf<List<Team>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    // Fetch teams for the selected sport
    LaunchedEffect(selectedSport) {
        isLoading = true
        db.collection("teams")
            .whereEqualTo("sport", selectedSport)
            .get()
            .addOnSuccessListener { documents ->
                teams = documents.mapNotNull { doc ->
                    try {
                        Team(
                            id = doc.id,
                            name = doc.getString("name") ?: "",
                            description = doc.getString("description") ?: "",
                            maxPlayers = doc.getLong("maxPlayers")?.toInt() ?: 0,
                            currentPlayers = doc.getLong("currentPlayers")?.toInt() ?: 0,
                            createdBy = doc.getString("createdBy") ?: "",
                            sport = doc.getString("sport") ?: "",
                            members = (doc.get("members") as? List<String>) ?: emptyList()
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                isLoading = false
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error loading teams: ${e.message}", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A237E), // Deep Blue
                        Color(0xFF0D47A1)  // Darker Blue
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Welcome to $selectedSport!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "What would you like to do?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E),
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = { 
                            navController.navigate(Screen.CreateTeam.createRoute(selectedSport))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1A237E)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            "Create New Team",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { 
                            navController.navigate(Screen.JoinTeam.createRoute(selectedSport))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1A237E)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            "Join Existing Team",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(16.dp),
                            color = Color(0xFF1A237E)
                        )
                    } else if (teams.isEmpty()) {
                        Text(
                            text = "No teams available for $selectedSport yet",
                            fontSize = 16.sp,
                            color = Color(0xFF1A237E),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        Text(
                            text = "Available Teams",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(teams) { team ->
                                SportTeamCard(
                                    team = team,
                                    onJoinClick = {
                                        // Check if team is full
                                        if (team.currentPlayers >= team.maxPlayers) {
                                            Toast.makeText(context, "Team is full", Toast.LENGTH_SHORT).show()
                                        } else {
                                            // Join the team
                                            db.collection("teams")
                                                .document(team.id)
                                                .update("currentPlayers", team.currentPlayers + 1)
                                                .addOnSuccessListener {
                                                    Toast.makeText(context, "Successfully joined team!", Toast.LENGTH_SHORT).show()
                                                    navController.navigate(Screen.ChatRoom.createRoute(team.id))
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(context, "Error joining team: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SportTeamCard(team: Team, onJoinClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = team.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E)
            )

            Text(
                text = team.description,
                fontSize = 14.sp,
                color = Color(0xFF1A237E).copy(alpha = 0.7f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Players: ${team.currentPlayers}/${team.maxPlayers}",
                    fontSize = 14.sp,
                    color = Color(0xFF1A237E)
                )

                Button(
                    onClick = onJoinClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A237E)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Join Team")
                }
            }
        }
    }
} 