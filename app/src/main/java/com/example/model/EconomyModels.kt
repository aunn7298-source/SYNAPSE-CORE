package com.example.model

enum class VipTier(val labelThai: String, val badgeColorHex: Long) {
    FREE("สมาชิกทั่วไป", 0xFF94A3B8),
    VIP("VIP Silver", 0xFF00F2FE),
    PRESIDENT_VIP("PRESIDENT VIP 👑", 0xFFFFD700)
}

enum class GachaRarity(val label: String, val colorHex: Long) {
    R("Rare", 0xFF00F2FE),
    SR("Super Rare", 0xFF8A2BE2),
    SSR("LEGENDARY SSR ✨", 0xFFFF007F)
}

data class GachaItem(
    val id: String,
    val name: String,
    val rarity: GachaRarity,
    val iconEmoji: String,
    val description: String
)

data class PaymentPackage(
    val id: String,
    val title: String,
    val priceBaht: Int,
    val coins: Int,
    val gemPoints: Int,
    val bonusDescription: String
)

data class DiscordAlert(
    val id: String,
    val timestamp: String,
    val title: String,
    val message: String,
    val channel: String = "#president-security-logs",
    val severity: String = "HIGH"
)

data class ManagedUser(
    val id: String,
    val name: String,
    val discordTag: String,
    val role: String,
    val isBanned: Boolean = false,
    val totalSpentBaht: Int = 0
)

data class AdminTrafficStats(
    val activeUsersCount: Int = 1428,
    val geminiLiveStreamingCount: Int = 45,
    val totalRevenueBaht: Int = 148500,
    val serverUptimeHours: String = "99.99% (720 ชม.)",
    val serverLatencyMs: Int = 12
)
