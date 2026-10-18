package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_records")
data class CallRecordEntity(
    @PrimaryKey val id: String,
    val contactName: String,
    val callType: String,
    val durationSeconds: Int,
    val wasScreenShared: Boolean,
    val timestampMillis: Long = System.currentTimeMillis(),
    val timeFormatted: String
)
