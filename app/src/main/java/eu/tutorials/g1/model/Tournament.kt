package eu.tutorials.g1.model

data class Tournament(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val sportId: String = "",
    val venueId: String = "",
    val startDate: Long = 0,
    val endDate: Long = 0,
    val registrationDeadline: Long = 0,
    val maxTeams: Int = 0,
    val currentTeams: Int = 0,
    val teams: List<Team> = emptyList(),
    val organizerId: String = "",
    val entryFee: Double = 0.0,
    val prizePool: Double = 0.0,
    val format: TournamentFormat = TournamentFormat.ROUND_ROBIN,
    val status: TournamentStatus = TournamentStatus.UPCOMING,
    val rules: List<String> = emptyList(),
    val schedule: List<Match> = emptyList(),
    val sponsors: List<Sponsor> = emptyList(),
    val isPrivate: Boolean = false,
    val inviteCode: String? = null,
    val skillLevel: SkillLevel = SkillLevel.ALL,
    val ageGroup: AgeGroup = AgeGroup.ALL,
    val gender: Gender = Gender.ALL
)

data class Team(
    val id: String = "",
    val name: String = "",
    val players: List<String> = emptyList(), // List of user IDs
    val captain: String = "", // User ID of captain
    val wins: Int = 0,
    val losses: Int = 0,
    val draws: Int = 0,
    val points: Int = 0,
    val status: TeamStatus = TeamStatus.REGISTERED,
    val description: String,
    val maxPlayers: Int,
    val currentPlayers: Int,
    val createdBy: String,
    val sport: String,
    val members: List<String>
)

data class Match(
    val id: String = "",
    val team1Id: String = "",
    val team2Id: String = "",
    val date: Long = 0,
    val startTime: String = "", // Format: "HH:mm"
    val endTime: String = "",   // Format: "HH:mm"
    val venueId: String = "",
    val score: Score? = null,
    val status: MatchStatus = MatchStatus.SCHEDULED,
    val round: Int = 0
)

data class Score(
    val team1Score: Int = 0,
    val team2Score: Int = 0,
    val details: Map<String, Int> = emptyMap() // For sports with multiple scoring categories
)

data class Sponsor(
    val id: String = "",
    val name: String = "",
    val logoUrl: String = "",
    val contribution: Double = 0.0,
    val benefits: List<String> = emptyList()
)

enum class TournamentFormat {
    ROUND_ROBIN,    // Each team plays against every other team
    KNOCKOUT,       // Single elimination
    DOUBLE_KNOCKOUT,// Double elimination
    GROUP_STAGE,    // Groups followed by knockout
    CUSTOM         // Custom format
}

enum class TournamentStatus {
    UPCOMING,
    REGISTRATION_OPEN,
    REGISTRATION_CLOSED,
    ONGOING,
    COMPLETED,
    CANCELLED
}

enum class TeamStatus {
    REGISTERED,
    CONFIRMED,
    ELIMINATED,
    WINNER,
    RUNNER_UP
}

enum class MatchStatus {
    SCHEDULED,
    ONGOING,
    COMPLETED,
    CANCELLED,
    POSTPONED
}

enum class AgeGroup {
    U16,    // Under 16
    U18,    // Under 18
    U21,    // Under 21
    OPEN,   // Open age
    SENIOR, // 35+
    ALL     // All ages
}

enum class Gender {
    MALE,
    FEMALE,
    MIXED,
    ALL
} 