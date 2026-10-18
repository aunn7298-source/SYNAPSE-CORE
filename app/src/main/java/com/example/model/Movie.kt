package com.example.model

data class Movie(
    val id: String,
    val title: String,
    val englishTitle: String,
    val duration: String,
    val rating: String,
    val genres: List<String>,
    val year: String,
    val description: String,
    val posterDrawableName: String = "movie_hero_cyberpunk",
    val isFeatured: Boolean = false,
    val totalSeconds: Int = 7440,
    val watchPartyActive: Boolean = false,
    val currentWatchers: Int = 128
)

data class WatchReaction(
    val id: String,
    val emoji: String,
    val sender: String,
    val xOffset: Float
)

data class RoomChatMessage(
    val id: String,
    val senderName: String,
    val message: String,
    val time: String,
    val isSystemSyncNotice: Boolean = false
)

data class WatchRoom(
    val roomId: String,
    val roomCode: String,
    val roomName: String,
    val movie: Movie,
    val hostName: String,
    val isPrivate: Boolean = true,
    val participants: List<String> = listOf("คุณ (Host)", "แพรว Ploy", "นนท์ Cyber"),
    val chatMessages: List<RoomChatMessage> = emptyList(),
    val isPlaybackSynced: Boolean = true
)
