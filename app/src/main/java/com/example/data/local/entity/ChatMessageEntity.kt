package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val senderName: String,
    val isSelf: Boolean,
    val content: String,
    val time: String,
    val messageType: String = "TEXT",
    val voiceDurationSeconds: Int = 0,
    val movieTitle: String? = null,
    val timestampMillis: Long = System.currentTimeMillis()
)
