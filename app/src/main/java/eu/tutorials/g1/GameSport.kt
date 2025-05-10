package eu.tutorials.g1

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSport(navController: NavHostController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // List of available sports with their icons
    val sports = listOf(
        Sport("Cricket", "🏏"),
        Sport("Football", "⚽"),
        Sport("Basketball", "🏀"),
        Sport("Tennis", "🎾"),
        Sport("Badminton", "🏸"),
        Sport("Volleyball", "🏐"),
        Sport("Hockey", "🏑"),
        Sport("Table Tennis", "🏓"),
        Sport("Swimming", "🏊"),
        Sport("Athletics", "🏃")
    )

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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Select Your Sport",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Choose the sport you want to play",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(sports) { sport ->
                    SportCard(
                        sport = sport,
                        onClick = {
                            // Save selected sport to user's profile
                            auth.currentUser?.uid?.let { uid ->
                                db.collection("users")
                                    .document(uid)
                                    .update("preferredSport", sport.name)
                                    .addOnSuccessListener {
                                        // Navigate to CreateOrJoin screen with selected sport
                                        navController.navigate(Screen.CreateOrJoin.createRoute(sport.name)) {
                                            popUpTo(Screen.SportSelection.route) { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Error saving sport preference: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SportCard(sport: Sport, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = sport.emoji,
                fontSize = 48.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = sport.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E),
                textAlign = TextAlign.Center
            )
        }
    }
}

data class Sport(
    val name: String,
    val emoji: String
) 