package eu.tutorials.g1

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeam(navController: NavHostController, selectedSport: String, sport: String) {
    var teamName by remember { mutableStateOf("") }
    var teamDescription by remember { mutableStateOf("") }
    var maxPlayers by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var preferredLocation by remember { mutableStateOf("") }
    var preferredTime by remember { mutableStateOf("") }
    var skillLevel by remember { mutableStateOf("") }
    var equipmentRequired by remember { mutableStateOf("") }
    var showLocationDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }
    var showSkillLevelDialog by remember { mutableStateOf(false) }
    var showMapDialog by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var customLocationName by remember { mutableStateOf("") }
    var showCustomTimeDialog by remember { mutableStateOf(false) }
    var locationFee by remember { mutableStateOf(0.0) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var customTimeInput by remember { mutableStateOf("") }
    var showTimeInputDialog by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val skillLevels = listOf("Beginner", "Intermediate", "Advanced", "Professional")
    val timeSlots = listOf(
        "Weekday Mornings (6AM-12PM)",
        "Weekday Afternoons (12PM-5PM)",
        "Weekday Evenings (5PM-10PM)",
        "Weekend Mornings (6AM-12PM)",
        "Weekend Afternoons (12PM-5PM)",
        "Weekend Evenings (5PM-10PM)"
    )

    // Location with time slots and fees
    data class LocationTimeSlot(
        val time: String,
        val fee: Double,
        val available: Boolean = true
    )
    
    data class LocationDetails(
        val name: String,
        val timeSlots: List<LocationTimeSlot>,
        val address: String,
        val contact: String
    )
    
    val locationDetails = mapOf(
        "Central Park" to LocationDetails(
            name = "Central Park",
            timeSlots = listOf(
                LocationTimeSlot("6:00 AM - 8:00 AM", 50.0),
                LocationTimeSlot("8:00 AM - 10:00 AM", 75.0),
                LocationTimeSlot("10:00 AM - 12:00 PM", 100.0),
                LocationTimeSlot("4:00 PM - 6:00 PM", 75.0),
                LocationTimeSlot("6:00 PM - 8:00 PM", 50.0)
            ),
            address = "123 Park Avenue, City Center",
            contact = "+1-234-567-8900"
        ),
        "Sports Complex" to LocationDetails(
            name = "Sports Complex",
            timeSlots = listOf(
                LocationTimeSlot("7:00 AM - 9:00 AM", 100.0),
                LocationTimeSlot("9:00 AM - 11:00 AM", 125.0),
                LocationTimeSlot("11:00 AM - 1:00 PM", 150.0),
                LocationTimeSlot("5:00 PM - 7:00 PM", 125.0),
                LocationTimeSlot("7:00 PM - 9:00 PM", 100.0)
            ),
            address = "456 Sports Lane, Downtown",
            contact = "+1-234-567-8901"
        ),
        // Add more locations with their time slots and fees
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A237E),
                        Color(0xFF0D47A1)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "Create $selectedSport Team",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            // Scrollable Content
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Basic Team Info Section
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Basic Information",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A237E)
                            )

                            OutlinedTextField(
                                value = teamName,
                                onValueChange = { teamName = it },
                                label = { Text("Team Name") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF1A237E),
                                    focusedLabelColor = Color(0xFF1A237E)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            OutlinedTextField(
                                value = teamDescription,
                                onValueChange = { teamDescription = it },
                                label = { Text("Team Description") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF1A237E),
                                    focusedLabelColor = Color(0xFF1A237E)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            OutlinedTextField(
                                value = maxPlayers,
                                onValueChange = { maxPlayers = it },
                                label = { Text("Maximum Players") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF1A237E),
                                    focusedLabelColor = Color(0xFF1A237E)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    Divider(
                        color = Color(0xFF1A237E).copy(alpha = 0.2f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Game Details Section
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Game Details",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A237E)
                            )

                            // Location Selection
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showLocationDialog = true },
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Preferred Location",
                                            fontSize = 16.sp,
                                            color = Color(0xFF1A237E)
                                        )
                                        if (preferredLocation.isNotEmpty()) {
                                            Text(
                                                text = preferredLocation,
                                                fontSize = 14.sp,
                                                color = Color(0xFF1A237E).copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = "Select Location",
                                        tint = Color(0xFF1A237E)
                                    )
                                }
                            }

                            // Preferred Time
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showTimeDialog = true },
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Preferred Time",
                                            fontSize = 16.sp,
                                            color = Color(0xFF1A237E)
                                        )
                                        if (preferredTime.isNotEmpty()) {
                                            Text(
                                                text = preferredTime,
                                                fontSize = 14.sp,
                                                color = Color(0xFF1A237E).copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                    Icon(
                                        Icons.Default.Schedule,
                                        contentDescription = "Select Time",
                                        tint = Color(0xFF1A237E)
                                    )
                                }
                            }

                            // Skill Level
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showSkillLevelDialog = true },
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Skill Level",
                                            fontSize = 16.sp,
                                            color = Color(0xFF1A237E)
                                        )
                                        if (skillLevel.isNotEmpty()) {
                                            Text(
                                                text = skillLevel,
                                                fontSize = 14.sp,
                                                color = Color(0xFF1A237E).copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = "Select Skill Level",
                                        tint = Color(0xFF1A237E)
                                    )
                                }
                            }

                            // Equipment Required
                            OutlinedTextField(
                                value = equipmentRequired,
                                onValueChange = { equipmentRequired = it },
                                label = { Text("Equipment Required") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF1A237E),
                                    focusedLabelColor = Color(0xFF1A237E)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Create Team Button
                    Button(
                        onClick = {
                            if (teamName.isBlank() || teamDescription.isBlank() || maxPlayers.isBlank() ||
                                preferredLocation.isBlank() || preferredTime.isBlank() || skillLevel.isBlank()) {
                                Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val maxPlayersInt = maxPlayers.toIntOrNull()
                            if (maxPlayersInt == null || maxPlayersInt <= 0) {
                                Toast.makeText(context, "Please enter a valid number of players", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isLoading = true
                            val team = hashMapOf(
                                "name" to teamName,
                                "description" to teamDescription,
                                "maxPlayers" to maxPlayersInt,
                                "currentPlayers" to 1,
                                "createdBy" to auth.currentUser?.uid,
                                "sport" to selectedSport,
                                "preferredLocation" to preferredLocation,
                                "preferredTime" to preferredTime,
                                "skillLevel" to skillLevel,
                                "equipmentRequired" to equipmentRequired
                            )

                            db.collection("teams")
                                .add(team)
                                .addOnSuccessListener { documentReference ->
                                    Toast.makeText(context, "Team created successfully!", Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.MyTeams.route)
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Error creating team: ${e.message}", Toast.LENGTH_SHORT).show()
                                    isLoading = false
                                }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1A237E)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                "Create Team",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    // Location Selection Dialog
    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            title = { Text("Select Location") },
            text = {
                Column {
                    locationDetails.keys.forEach { location ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    preferredLocation = location
                                    showLocationDialog = false
                                    // Show time slots for selected location
                                    showTimeDialog = true
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = location,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A237E)
                                )
                                Text(
                                    text = locationDetails[location]?.address ?: "",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                    TextButton(
                        onClick = {
                            showLocationDialog = false
                            showMapDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Select Custom Location on Map")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Map Selection Dialog
    if (showMapDialog) {
        AlertDialog(
            onDismissRequest = { showMapDialog = false },
            title = { Text("Select Location on Map") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Map View using Compose
                    val defaultLocation = LatLng(0.0, 0.0)
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(defaultLocation, 15f)
                    }
                    
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(isMyLocationEnabled = true),
                        onMapClick = { latLng ->
                            selectedLocation = latLng
                        }
                    ) {
                        selectedLocation?.let { location ->
                            Marker(
                                state = MarkerState(position = location),
                                title = "Selected Location"
                            )
                        }
                    }

                    // Custom Location Name Input
                    OutlinedTextField(
                        value = customLocationName,
                        onValueChange = { customLocationName = it },
                        label = { Text("Location Name (e.g., My Home Ground)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1A237E),
                            focusedLabelColor = Color(0xFF1A237E)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Selected Location Display
                    if (selectedLocation != null) {
                        Text(
                            text = "Selected: ${selectedLocation?.latitude}, ${selectedLocation?.longitude}",
                            fontSize = 14.sp,
                            color = Color(0xFF1A237E)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (selectedLocation != null && customLocationName.isNotEmpty()) {
                            preferredLocation = "$customLocationName (${selectedLocation?.latitude}, ${selectedLocation?.longitude})"
                            showMapDialog = false
                        } else {
                            Toast.makeText(
                                context,
                                "Please select a location on the map and provide a name",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text("Confirm Location")
                }
            },
            dismissButton = {
                TextButton(onClick = { showMapDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Time Selection Dialog
    if (showTimeDialog) {
        AlertDialog(
            onDismissRequest = { showTimeDialog = false },
            title = { Text("Select Time") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (preferredLocation in locationDetails.keys) {
                        // Show predefined time slots for selected location
                        locationDetails[preferredLocation]?.timeSlots?.forEach { slot ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        preferredTime = slot.time
                                        locationFee = slot.fee
                                        showTimeDialog = false
                                        showPaymentDialog = true
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = slot.time,
                                            fontSize = 16.sp,
                                            color = Color(0xFF1A237E)
                                        )
                                        Text(
                                            text = "Fee: $${slot.fee}",
                                            fontSize = 14.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    if (slot.available) {
                                        Icon(
                                            Icons.Default.Schedule,
                                            contentDescription = "Available",
                                            tint = Color.Green
                                        )
                                    } else {
                                        Icon(
                                            Icons.Default.Schedule,
                                            contentDescription = "Unavailable",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Custom Time Input Option
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showTimeInputDialog = true },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Enter Custom Time",
                                fontSize = 16.sp,
                                color = Color(0xFF1A237E)
                            )
                            Text(
                                text = "Type your preferred time (e.g., 'Every Monday 6-8 PM' or 'Weekends 2-4 PM')",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    // Show current time selection
                    if (preferredTime.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Selected Time",
                                        fontSize = 16.sp,
                                        color = Color(0xFF1A237E)
                                    )
                                    Text(
                                        text = preferredTime,
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                }
                                IconButton(
                                    onClick = { preferredTime = "" }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Clear",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (preferredTime.isNotEmpty()) {
                            showTimeDialog = false
                        } else {
                            Toast.makeText(context, "Please select or enter a time", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Custom Time Input Dialog
    if (showTimeInputDialog) {
        AlertDialog(
            onDismissRequest = { showTimeInputDialog = false },
            title = { Text("Enter Preferred Time") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = customTimeInput,
                        onValueChange = { customTimeInput = it },
                        label = { Text("Enter your preferred time") },
                        placeholder = { Text("e.g., 'Every Monday 6-8 PM' or 'Weekends 2-4 PM'") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1A237E),
                            focusedLabelColor = Color(0xFF1A237E)
                        )
                    )

                    // Suggestions
                    Text(
                        text = "Examples:",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "• Every Monday 6-8 PM\n" +
                              "• Weekends 2-4 PM\n" +
                              "• Weekday evenings 7-9 PM\n" +
                              "• First Sunday of every month 10 AM-12 PM",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (customTimeInput.isNotEmpty()) {
                            preferredTime = customTimeInput
                            showTimeInputDialog = false
                            showTimeDialog = false
                        } else {
                            Toast.makeText(context, "Please enter a time", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimeInputDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Skill Level Dialog
    if (showSkillLevelDialog) {
        AlertDialog(
            onDismissRequest = { showSkillLevelDialog = false },
            title = { Text("Select Skill Level") },
            text = {
                Column {
                    skillLevels.forEach { level ->
                        TextButton(
                            onClick = {
                                skillLevel = level
                                showSkillLevelDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(level)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSkillLevelDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

data class PlayerDetails(
    val experience: String = "",
    val preferredPosition: String = "",
    val skills: String = ""
)

enum class LocationType {
    CUSTOM,
    PREDEFINED
}

private val predefinedLocations = listOf(
    "Central Park",
    "Sports Complex",
    "Community Center",
    "University Ground",
    "City Stadium",
    "Local Sports Club",
    "Public Playground",
    "Indoor Sports Center"
)
