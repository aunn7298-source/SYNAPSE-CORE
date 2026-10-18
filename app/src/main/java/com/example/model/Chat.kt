package com.example.model

enum class MessageType {
    TEXT,
    VOICE_NOTE,
    MOVIE_INVITE,
    SCREEN_SHARE_CALL
}

data class ChatMessage(
    val id: String,
    val senderName: String,
    val isSelf: Boolean,
    val content: String,
    val time: String,
    val messageType: MessageType = MessageType.TEXT,
    val voiceDurationSeconds: Int = 0,
    val voiceWaveformAmplitudes: List<Float> = emptyList(),
    val isPlayingVoice: Boolean = false,
    val voiceCurrentProgress: Float = 0f,
    val movieTitle: String? = null
)

data class ChatConversation(
    val id: String,
    val contactName: String,
    val avatarColor: Long,
    val isOnline: Boolean,
    val statusText: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val isEncrypted: Boolean = true,
    val messages: List<ChatMessage> = emptyList()
)
