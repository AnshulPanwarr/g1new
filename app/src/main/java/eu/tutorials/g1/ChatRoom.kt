package eu.tutorials.g1

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import java.text.SimpleDateFormat
import java.util.*

// Enhanced message model
data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val status: MessageStatus = MessageStatus.SENT,
    val reactions: Map<String, String> = mapOf(), // userId to reaction emoji
    val attachments: List<Attachment> = listOf(),
    val isEdited: Boolean = false
)

data class Attachment(
    val type: AttachmentType,
    val url: String,
    val name: String = "",
    val size: Long = 0
)

enum class AttachmentType {
    IMAGE, FILE
}

enum class MessageStatus {
    SENT, DELIVERED, READ
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoom(navController: NavHostController?, teamId: String) {
    var message by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }
    var teamName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val listState = rememberLazyListState()

    // Fetch team details
    LaunchedEffect(teamId) {
        db.collection("teams")
            .document(teamId)
            .get()
            .addOnSuccessListener { document ->
                teamName = document.getString("name") ?: "Team Chat"
                isLoading = false
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error loading team: ${e.message}", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
    }

    // Listen for messages
    LaunchedEffect(teamId) {
        db.collection("teams")
            .document(teamId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(context, "Error loading messages: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    messages = snapshot.documents.mapNotNull { doc ->
                        try {
                            ChatMessage(
                                id = doc.id,
                                text = doc.getString("text") ?: "",
                                senderId = doc.getString("senderId") ?: "",
                                sender = doc.getString("senderName") ?: "Anonymous",
                                timestamp = doc.getTimestamp("timestamp")?.toDate()?.time ?: 0
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                }
            }
    }

    // Scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = if (isLoading) "Loading..." else teamName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController?.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A237E)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)) // Light gray background for better contrast
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Increased spacing between messages
                ) {
                    items(messages) { message ->
                        ChatMessageItem(
                            message = message,
                            isCurrentUser = message.senderId == currentUser?.uid
                        )
                    }
                }

                // Enhanced message input area
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(24.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            placeholder = { Text("Type a message...") },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF1A237E),
                                unfocusedBorderColor = Color(0xFF1A237E).copy(alpha = 0.5f),
                                focusedLabelColor = Color(0xFF1A237E),
                                unfocusedLabelColor = Color(0xFF1A237E).copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(20.dp),
                            maxLines = 4
                        )

                        IconButton(
                            onClick = {
                                if (message.isNotBlank() && currentUser != null) {
                                    val messageData = mapOf(
                                        "text" to message,
                                        "senderId" to currentUser.uid,
                                        "senderName" to (currentUser.displayName ?: "Anonymous"),
                                        "timestamp" to FieldValue.serverTimestamp()
                                    )

                                    db.collection("teams")
                                        .document(teamId)
                                        .collection("messages")
                                        .add(messageData)
                                        .addOnSuccessListener {
                                            message = ""
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(context, "Error sending message: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "Send",
                                tint = Color(0xFF1A237E)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage, isCurrentUser: Boolean) {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
    ) {
        // User name and time header with enhanced visibility
        Row(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .background(
                    color = if (isCurrentUser) Color(0xFF1A237E).copy(alpha = 0.1f) else Color(0xFF1A237E).copy(alpha = 0.05f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isCurrentUser) {
                Text(
                    text = message.sender,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Text(
                text = dateFormat.format(Date(message.timestamp)),
                fontSize = 12.sp,
                color = Color(0xFF1A237E).copy(alpha = 0.7f)
            )
        }

        // Enhanced message bubble
        Card(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                        bottomEnd = if (isCurrentUser) 4.dp else 16.dp
                    )
                ),
            colors = CardDefaults.cardColors(
                containerColor = if (isCurrentUser) Color(0xFF1A237E) else Color.White
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                bottomEnd = if (isCurrentUser) 4.dp else 16.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = message.text,
                    color = if (isCurrentUser) Color.White else Color(0xFF1A237E),
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                
                // Enhanced message status indicator
                if (isCurrentUser) {
                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .align(Alignment.End),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (message.status) {
                                MessageStatus.SENT -> "✓"
                                MessageStatus.DELIVERED -> "✓✓"
                                MessageStatus.READ -> "✓✓"
                            },
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        if (message.isEdited) {
                            Text(
                                text = " (edited)",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChatRoom() {
    ChatRoom(navController = null, teamId = "team1")
}
