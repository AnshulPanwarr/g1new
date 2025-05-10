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
import eu.tutorials.g1.model.Event
import eu.tutorials.g1.model.Venue
import eu.tutorials.g1.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Events", "Venues", "Nearby")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Discover") },
                actions = {
                    IconButton(onClick = { /* TODO: Open filters */ }) {
                        Icon(Icons.Default.FilterList, "Filter")
                    }
                    IconButton(onClick = { /* TODO: Open map view */ }) {
                        Icon(Icons.Default.Map, "Map View")
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
            // Tab Row
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // Content based on selected tab
            when (selectedTab) {
                0 -> EventsList()
                1 -> VenuesList()
                2 -> NearbyList()
            }
        }
    }
}

@Composable
fun EventsList() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TODO: Replace with actual events from ViewModel
        items(sampleEvents) { event ->
            EventCard(event)
        }
    }
}

@Composable
fun VenuesList() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TODO: Replace with actual venues from ViewModel
        items(sampleVenues) { venue ->
            VenueCard(venue)
        }
    }
}

@Composable
fun NearbyList() {
    // TODO: Implement nearby venues/events using location services
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Nearby venues and events will be shown here")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* TODO: Navigate to event details */ }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Date: ${event.date}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${event.currentParticipants}/${event.maxParticipants}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenueCard(venue: Venue) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* TODO: Navigate to venue details */ }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = venue.name,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = venue.address,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rating: ${venue.rating}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "₹${venue.pricePerHour}/hr",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// Sample data for preview
private val sampleEvents = listOf(
    Event(
        id = "1",
        title = "Weekend Football Match",
        description = "Casual football match for all skill levels",
        date = System.currentTimeMillis(),
        maxParticipants = 20,
        currentParticipants = 12
    ),
    Event(
        id = "2",
        title = "Basketball Tournament",
        description = "3v3 basketball tournament with prizes",
        date = System.currentTimeMillis() + 86400000,
        maxParticipants = 12,
        currentParticipants = 8
    )
)

private val sampleVenues = listOf(
    Venue(
        id = "1",
        name = "Sports Complex",
        address = "123 Main Street, City",
        pricePerHour = 1000.0,
        rating = 4.5
    ),
    Venue(
        id = "2",
        name = "Community Ground",
        address = "456 Park Avenue, City",
        pricePerHour = 500.0,
        rating = 4.2
    )
) 