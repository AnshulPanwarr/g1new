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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import eu.tutorials.g1.model.Team

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTeams(navController: NavHostController) {
    var teams by remember { mutableStateOf<List<Team>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // Fetch user's teams
    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            try {
                db.collection("teams")
                    .whereArrayContains("members", currentUser.uid)
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
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        } else {
            Toast.makeText(context, "Please sign in to view your teams", Toast.LENGTH_SHORT).show()
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
                        text = "My Teams",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E),
                        textAlign = TextAlign.Center
                    )

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = Color(0xFF1A237E)
                        )
                    } else if (teams.isEmpty()) {
                        Text(
                            text = "You haven't joined any teams yet",
                            color = Color(0xFF1A237E),
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = { navController.navigate(Screen.JoinTeam.route) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1A237E)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Join a Team")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(teams) { team ->
                                MyTeamCard(
                                    team = team,
                                    onTeamClick = {
                                        navController.navigate(Screen.ChatRoom.createRoute(team.id))
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
fun MyTeamCard(team: Team, onTeamClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = onTeamClick
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

                Text(
                    text = team.sport,
                    fontSize = 14.sp,
                    color = Color(0xFF1A237E)
                )
            }
        }
    }
} 