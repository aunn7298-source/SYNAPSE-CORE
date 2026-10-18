package com.example.model

data class ClipItem(
    val id: String,
    val creatorName: String,
    val creatorHandle: String,
    val title: String,
    val tags: List<String>,
    val soundTrack: String,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val privacyLabel: String = "สาธารณะ",
    val gradientColors: List<Long>
)

data class ClipComment(
    val id: String,
    val author: String,
    val text: String,
    val timeAgo: String,
    val likes: Int = 0
)
