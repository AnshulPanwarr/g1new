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
import com.google.firebase.firestore.FieldValue
import eu.tutorials.g1.model.Team

private const val TAG = "JoinTeam"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinTeam(navController: NavHostController, sport1: String) {
    var selectedSport by remember { mutableStateOf(sport1) }
    var teams by remember { mutableStateOf<List<Team>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var showDropdown by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // List of available sports
    val sports = listOf(
        "Cricket",
        "Football",
        "Basketball",
        "Tennis",
        "Badminton",
        "Volleyball",
        "Hockey",
        "Table Tennis",
        "Swimming",
        "Athletics"
    )

    // Fetch teams when sport is selected
    LaunchedEffect(selectedSport) {
        if (selectedSport.isNotEmpty()) {
            isLoading = true
            try {
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
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
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
                        text = "Join a Team",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E),
                        textAlign = TextAlign.Center
                    )

                    // Sport Selection Dropdown
                    ExposedDropdownMenuBox(
                        expanded = showDropdown,
                        onExpandedChange = { showDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = selectedSport,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Select Sport") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showDropdown) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1A237E),
                                focusedLabelColor = Color(0xFF1A237E)
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = showDropdown,
                            onDismissRequest = { showDropdown = false }
                        ) {
                            sports.forEach { sport ->
                                DropdownMenuItem(
                                    text = { Text(sport) },
                                    onClick = { 
                                        selectedSport = sport
                                        showDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = Color(0xFF1A237E)
                        )
                    } else if (teams.isEmpty()) {
                        Text(
                            text = if (selectedSport.isEmpty()) 
                                "Select a sport to see available teams" 
                            else 
                                "No teams available for $selectedSport",
                            color = Color(0xFF1A237E),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(teams) { team ->
                                JoinTeamCard(
                                    team = team,
                                    onJoinClick = {
                                        val currentUser = auth.currentUser
                                        if (currentUser != null) {
                                            // Check if team is full
                                            if (team.currentPlayers >= team.maxPlayers) {
                                                Toast.makeText(context, "Team is full", Toast.LENGTH_SHORT).show()
                                                return@JoinTeamCard
                                            }

                                            // Check if user is already a member
                                            if (team.members.contains(currentUser.uid)) {
                                                Toast.makeText(context, "You are already a member of this team", Toast.LENGTH_SHORT).show()
                                                return@JoinTeamCard
                                            }

                                            // Add user to team
                                            val member = hashMapOf(
                                                "userId" to currentUser.uid,
                                                "joinedAt" to FieldValue.serverTimestamp(),
                                                "role" to "member"
                                            )

                                            db.collection("teams")
                                                .document(team.id)
                                                .collection("members")
                                                .document(currentUser.uid)
                                                .set(member)
                                                .addOnSuccessListener {
                                                    // Update team's current players count and members list
                                                    db.collection("teams")
                                                        .document(team.id)
                                                        .update(
                                                            mapOf(
                                                                "currentPlayers" to FieldValue.increment(1),
                                                                "members" to FieldValue.arrayUnion(currentUser.uid)
                                                            )
                                                        )
                                                        .addOnSuccessListener {
                                                            Toast.makeText(context, "Successfully joined team!", Toast.LENGTH_SHORT).show()
                                                            navController.navigate(Screen.ChatRoom.createRoute(team.id)) {
                                                                popUpTo(Screen.JoinTeam.route) { inclusive = true }
                                                            }
                                                        }
                                                        .addOnFailureListener { e ->
                                                            Toast.makeText(context, "Error updating team: ${e.message}", Toast.LENGTH_SHORT).show()
                                                        }
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(context, "Error joining team: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            Toast.makeText(context, "Please sign in to join a team", Toast.LENGTH_SHORT).show()
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
fun JoinTeamCard(team: Team, onJoinClick: () -> Unit) {
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
