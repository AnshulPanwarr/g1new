package eu.tutorials.g1.model

data class Subscription(
    val id: String = "",
    val userId: String = "",
    val plan: SubscriptionPlan = SubscriptionPlan.FREE,
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long = System.currentTimeMillis(),
    val isActive: Boolean = false,
    val autoRenew: Boolean = false,
    val paymentMethod: String = ""
)

enum class SubscriptionPlan(
    val price: Double,
    val duration: Int, // in months
    val features: List<SubscriptionFeature>
) {
    FREE(
        price = 0.0,
        duration = 1,
        features = listOf(
            SubscriptionFeature.CREATE_EVENTS,
            SubscriptionFeature.JOIN_EVENTS,
            SubscriptionFeature.BASIC_CHAT
        )
    ),
    BASIC(
        price = 99.0,
        duration = 1,
        features = listOf(
            SubscriptionFeature.CREATE_EVENTS,
            SubscriptionFeature.JOIN_EVENTS,
            SubscriptionFeature.BASIC_CHAT,
            SubscriptionFeature.CREATE_TOURNAMENTS,
            SubscriptionFeature.ADVANCED_STATS,
            SubscriptionFeature.PLAYER_RATINGS
        )
    ),
    PREMIUM(
        price = 299.0,
        duration = 1,
        features = listOf(
            SubscriptionFeature.CREATE_EVENTS,
            SubscriptionFeature.JOIN_EVENTS,
            SubscriptionFeature.BASIC_CHAT,
            SubscriptionFeature.CREATE_TOURNAMENTS,
            SubscriptionFeature.ADVANCED_STATS,
            SubscriptionFeature.PLAYER_RATINGS,
            SubscriptionFeature.PRIZE_POOLS,
            SubscriptionFeature.SPONSORSHIP,
            SubscriptionFeature.ADVANCED_ANALYTICS,
            SubscriptionFeature.PRIORITY_SUPPORT
        )
    )
}

enum class SubscriptionFeature {
    CREATE_EVENTS,          // Create and manage events
    JOIN_EVENTS,           // Join events created by others
    BASIC_CHAT,            // Basic chat functionality
    CREATE_TOURNAMENTS,    // Create and manage tournaments
    ADVANCED_STATS,        // Detailed player and team statistics
    PLAYER_RATINGS,        // Rate and review players
    PRIZE_POOLS,           // Set up prize pools for tournaments
    SPONSORSHIP,           // Sponsor tournaments and events
    ADVANCED_ANALYTICS,    // Detailed analytics and insights
    PRIORITY_SUPPORT       // Priority customer support
} 