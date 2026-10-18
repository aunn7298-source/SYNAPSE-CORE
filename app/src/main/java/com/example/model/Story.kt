package com.example.model

data class StoryItem(
    val id: String,
    val userId: String,
    val userName: String,
    val userAvatarUrl: String = "",
    val caption: String,
    val timeAgo: String,
    val musicTrack: String? = null,
    val backgroundGradientColors: List<Long>,
    val stickerEmoji: String = "🔥",
    val isMyStory: Boolean = false,
    val isViewed: Boolean = false
)
