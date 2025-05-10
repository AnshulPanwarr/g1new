package eu.tutorials.g1.model

data class Sport(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val minPlayers: Int = 0,
    val maxPlayers: Int = 0,
    val category: SportCategory = SportCategory.OTHER,
    val imageUrl: String = "",
    val rules: List<String> = emptyList(),
    val equipment: List<String> = emptyList()
)

enum class SportCategory {
    TEAM_SPORTS,    // Football, Basketball, Cricket, etc.
    INDIVIDUAL,     // Tennis, Badminton, etc.
    COMBAT,         // Boxing, MMA, etc.
    FITNESS,        // Yoga, CrossFit, etc.
    OUTDOOR,        // Hiking, Rock Climbing, etc.
    OTHER
} 