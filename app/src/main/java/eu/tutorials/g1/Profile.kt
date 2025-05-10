package eu.tutorials.g1

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavHostController) {
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userBio by remember { mutableStateOf("") }
    var userPhone by remember { mutableStateOf("") }
    var userGender by remember { mutableStateOf("") }
    var userAge by remember { mutableStateOf("") }
    var userLocation by remember { mutableStateOf("") }
    var userPreferredSports by remember { mutableStateOf("") }
    var userExperience by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(true) }
    var showEditDialog by remember { mutableStateOf(false) }
    
    // Edit dialog states
    var editedName by remember { mutableStateOf("") }
    var editedBio by remember { mutableStateOf("") }
    var editedPhone by remember { mutableStateOf("") }
    var editedGender by remember { mutableStateOf("") }
    var editedAge by remember { mutableStateOf("") }
    var editedLocation by remember { mutableStateOf("") }
    var editedPreferredSports by remember { mutableStateOf("") }
    var editedExperience by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    val currentUser = auth.currentUser
    val scrollState = rememberScrollState()

    // Image picker launcher
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val storageRef = storage.reference.child("profile_images/${currentUser?.uid}")
            storageRef.putFile(it)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        currentUser?.uid?.let { uid ->
                            db.collection("users").document(uid)
                                .update("imageUrl", downloadUri.toString())
                                .addOnSuccessListener {
                                    imageUrl = downloadUri.toString()
                                    Toast.makeText(context, "Profile image updated successfully", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Error updating profile image: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error uploading image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    LaunchedEffect(Unit) {
        if (currentUser == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Profile.route) { inclusive = true }
            }
            return@LaunchedEffect
        }

        currentUser.uid.let { uid ->
            db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        userName = document.getString("name") ?: "User"
                        userEmail = document.getString("email") ?: currentUser.email ?: ""
                        userBio = document.getString("bio") ?: "No bio yet"
                        userPhone = document.getString("phone") ?: ""
                        userGender = document.getString("gender") ?: ""
                        userAge = document.getString("age") ?: ""
                        userLocation = document.getString("location") ?: ""
                        userPreferredSports = document.getString("preferredSports") ?: ""
                        userExperience = document.getString("experience") ?: ""
                        imageUrl = document.getString("imageUrl") ?: ""
                        loading = false
                    } else {
                        Toast.makeText(context, "Error: User profile not found", Toast.LENGTH_SHORT).show()
                        loading = false
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error loading profile: ${e.message}", Toast.LENGTH_SHORT).show()
                    loading = false
                }
        }
    }

    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFF1A237E), // Deep Blue
                            Color(0xFF0D47A1), // Darker Blue
                            Color(0xFF1A237E)  // Deep Blue
                        ),
                        center = androidx.compose.ui.geometry.Offset.Zero
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Profile Header
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 16.dp,
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
                        // Profile Image
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF64B5F6), // Light Blue
                                            Color(0xFF1976D2)  // Medium Blue
                                        )
                                    )
                                )
                                .clickable { imagePicker.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            if (imageUrl.isNotEmpty()) {
                                Image(
                                    painter = rememberAsyncImagePainter(imageUrl),
                                    contentDescription = "Profile Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Text(
                                    text = userName.firstOrNull()?.toString() ?: "U",
                                    fontSize = 56.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // User Info
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = userName,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A237E)
                            )
                            Text(
                                text = userEmail,
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Profile Details
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.95f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ProfileDetailItem(
                            icon = Icons.Default.Info,
                            label = "Bio",
                            value = userBio
                        )
                        ProfileDetailItem(
                            icon = Icons.Default.Phone,
                            label = "Phone",
                            value = userPhone
                        )
                        ProfileDetailItem(
                            icon = Icons.Default.Person,
                            label = "Gender",
                            value = userGender
                        )
                        ProfileDetailItem(
                            icon = Icons.Default.Cake,
                            label = "Age",
                            value = userAge
                        )
                        ProfileDetailItem(
                            icon = Icons.Default.LocationOn,
                            label = "Location",
                            value = userLocation
                        )
                        ProfileDetailItem(
                            icon = Icons.Default.Sports,
                            label = "Preferred Sports",
                            value = userPreferredSports
                        )
                        ProfileDetailItem(
                            icon = Icons.Default.Star,
                            label = "Experience",
                            value = userExperience
                        )
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { showEditDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1A237E)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Edit Profile",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            auth.signOut()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Profile.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB71C1C)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Sign Out"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Sign Out",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // Edit Profile Dialog
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Profile") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedBio,
                        onValueChange = { editedBio = it },
                        label = { Text("Bio") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPhone,
                        onValueChange = { editedPhone = it },
                        label = { Text("Phone") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedGender,
                        onValueChange = { editedGender = it },
                        label = { Text("Gender") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedAge,
                        onValueChange = { editedAge = it },
                        label = { Text("Age") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedLocation,
                        onValueChange = { editedLocation = it },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPreferredSports,
                        onValueChange = { editedPreferredSports = it },
                        label = { Text("Preferred Sports") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedExperience,
                        onValueChange = { editedExperience = it },
                        label = { Text("Experience") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        currentUser?.uid?.let { uid ->
                            val updates = hashMapOf<String, Any>(
                                "name" to editedName,
                                "bio" to editedBio,
                                "phone" to editedPhone,
                                "gender" to editedGender,
                                "age" to editedAge,
                                "location" to editedLocation,
                                "preferredSports" to editedPreferredSports,
                                "experience" to editedExperience
                            )
                            
                            db.collection("users").document(uid)
                                .update(updates)
                                .addOnSuccessListener {
                                    userName = editedName
                                    userBio = editedBio
                                    userPhone = editedPhone
                                    userGender = editedGender
                                    userAge = editedAge
                                    userLocation = editedLocation
                                    userPreferredSports = editedPreferredSports
                                    userExperience = editedExperience
                                    showEditDialog = false
                                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Error updating profile: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEditDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileDetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF1A237E),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = value.ifEmpty { "Not specified" },
                fontSize = 16.sp,
                color = Color(0xFF1A237E),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
} 