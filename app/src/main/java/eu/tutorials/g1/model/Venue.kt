package eu.tutorials.g1.model

import com.google.android.gms.maps.model.LatLng

data class Venue(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val address: String = "",
    val location: LatLng? = null,
    val sports: List<String> = emptyList(), // List of sport IDs
    val facilities: List<Facility> = emptyList(),
    val openingHours: Map<DayOfWeek, TimeRange> = emptyMap(),
    val pricePerHour: Double = 0.0,
    val images: List<String> = emptyList(),
    val rating: Double = 0.0,
    val reviews: List<Review> = emptyList(),
    val contactNumber: String = "",
    val website: String = "",
    val isVerified: Boolean = false
)

data class Facility(
    val name: String,
    val description: String,
    val isAvailable: Boolean = true,
    val price: Double = 0.0
)

data class TimeRange(
    val open: String, // Format: "HH:mm"
    val close: String // Format: "HH:mm"
)

data class Review(
    val userId: String,
    val userName: String,
    val rating: Double,
    val comment: String,
    val date: Long = System.currentTimeMillis()
)

enum class DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
} 