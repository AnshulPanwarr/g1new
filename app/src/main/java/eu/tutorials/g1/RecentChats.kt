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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

data class RecentChat(
    val teamId: String,
    val teamName: String,
    val lastMessage: String,
    val timestamp: Date,
    val unreadCount: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentChats(navController: NavHostController) {
    var recentChats by remember { mutableStateOf<List<RecentChat>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    LaunchedEffect(currentUser?.uid) {
        if (currentUser != null) {
            // First get all teams the user is a member of
            db.collection("teams")
                .whereArrayContains("members", currentUser.uid)
                .get()
                .addOnSuccessListener { teamDocuments ->
                    val chatPromises = teamDocuments.map { teamDoc ->
                        val teamId = teamDoc.id
                        val teamName = teamDoc.getString("name") ?: "Team"
                        
                        // Get the last message for each team
                        db.collection("teams")
                            .document(teamId)
                            .collection("messages")
                            .orderBy("timestamp")
                            .limit(1)
                            .get()
                            .addOnSuccessListener { messageDocs ->
                                if (messageDocs.documents.isNotEmpty()) {
                                    val lastMessage = messageDocs.documents[0]
                                    val chat = RecentChat(
                                        teamId = teamId,
                                        teamName = teamName,
                                        lastMessage = lastMessage.getString("text") ?: "",
                                        timestamp = lastMessage.getTimestamp("timestamp")?.toDate() ?: Date(),
                                        unreadCount = 0 // TODO: Implement unread count
                                    )
                                    recentChats = (recentChats + chat).sortedByDescending { it.timestamp }
                                }
                            }
                    }
                    
                    // Wait for all chat promises to complete
                    if (chatPromises.isEmpty()) {
                        isLoading = false
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error loading chats: ${e.message}", Toast.LENGTH_SHORT).show()
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Recent Chats",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (recentChats.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.95f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No recent chats",
                            fontSize = 16.sp,
                            color = Color(0xFF1A237E)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate(Screen.MyTeams.route) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1A237E)
                            )
                        ) {
                            Text("View My Teams")
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recentChats) { chat ->
                        ChatCard(
                            chat = chat,
                            onClick = {
                                navController.navigate(Screen.ChatRoom.createRoute(chat.teamId))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatCard(chat: RecentChat, onClick: () -> Unit) {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    
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
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.teamName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )
                Text(
                    text = dateFormat.format(chat.timestamp),
                    fontSize = 12.sp,
                    color = Color(0xFF1A237E).copy(alpha = 0.7f)
                )
            }

            Text(
                text = chat.lastMessage,
                fontSize = 14.sp,
                color = Color(0xFF1A237E).copy(alpha = 0.7f),
                maxLines = 2
            )

            if (chat.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = Color(0xFF1A237E),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = chat.unreadCount.toString(),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
} 