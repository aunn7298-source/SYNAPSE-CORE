package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CallType
import com.example.model.ChatConversation
import com.example.model.ChatMessage
import com.example.model.MessageType
import com.example.ui.components.AudioRecordBar
import com.example.ui.components.VoiceNoteBubble
import com.example.ui.theme.*

@Composable
fun ChatDetailScreen(
    conversation: ChatConversation,
    isRecordingVoice: Boolean,
    recordingDurationSeconds: Int,
    onBack: () -> Unit,
    onStartCall: (String, CallType) -> Unit,
    onSendText: (String) -> Unit,
    onStartVoiceRecord: () -> Unit,
    onStopAndSendVoiceRecord: () -> Unit,
    onCancelVoiceRecord: () -> Unit,
    onTogglePlayVoice: (String) -> Unit
) {
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = ObsidianBlack,
        topBar = {
            Surface(
                color = ObsidianSurface,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "กลับ",
                            tint = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(conversation.avatarColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = conversation.contactName.take(1),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = conversation.contactName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 1
                        )
                        Text(
                            text = if (conversation.isOnline) "🟢 ออนไลน์ • E2EE" else conversation.statusText,
                            color = if (conversation.isOnline) NeonEmerald else TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    // Voice Call Button
                    IconButton(
                        onClick = { onStartCall(conversation.contactName, CallType.VOICE) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "โทรด้วยเสียง",
                            tint = NeonCyan
                        )
                    }

                    // Video Call Button (With Screen Sharing capabilities)
                    IconButton(
                        onClick = { onStartCall(conversation.contactName, CallType.VIDEO) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "วิดีโอคอล",
                            tint = NeonViolet
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = ObsidianSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                if (isRecordingVoice) {
                    AudioRecordBar(
                        durationSeconds = recordingDurationSeconds,
                        onCancel = onCancelVoiceRecord,
                        onSend = onStopAndSendVoiceRecord
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Mic Button for voice message
                        IconButton(
                            onClick = onStartVoiceRecord,
                            modifier = Modifier
                                .size(42.dp)
                                .background(NeonCyan.copy(alpha = 0.15f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "บันทึกเสียง",
                                tint = NeonCyan
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Text Field
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("พิมพ์ข้อความปลอดภัย...", color = TextMuted, fontSize = 13.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ObsidianInput,
                                unfocusedContainerColor = ObsidianInput,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = ObsidianCardBorder
                            ),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Send Button
                        IconButton(
                            onClick = {
                                if (inputText.isNotBlank()) {
                                    onSendText(inputText)
                                    inputText = ""
                                }
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .background(NeonCyan, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "ส่งข้อความ",
                                tint = ObsidianBlack,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Security Info Tag
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        Text(
                            text = "🔒 ข้อความและเสียงในแชทนี้เข้ารหัส E2EE เฉพาะคุณกับคู่สนทนา",
                            color = TextMuted,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            items(conversation.messages) { message ->
                MessageRowItem(
                    message = message,
                    onTogglePlayVoice = { onTogglePlayVoice(message.id) }
                )
            }
        }
    }
}

@Composable
fun MessageRowItem(
    message: ChatMessage,
    onTogglePlayVoice: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isSelf) Alignment.End else Alignment.Start
    ) {
        when (message.messageType) {
            MessageType.VOICE_NOTE -> {
                VoiceNoteBubble(
                    message = message,
                    onTogglePlay = onTogglePlayVoice
                )
            }
            MessageType.MOVIE_INVITE -> {
                Surface(
                    color = Color(0xFF23143A),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.Brush.linearGradient(listOf(NeonViolet, NeonMagenta))),
                    modifier = Modifier.widthIn(max = 280.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Movie, contentDescription = null, tint = NeonViolet, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "คำเชิญ Watch Party", color = Color(0xFFD6BBFB), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = message.movieTitle ?: "ภาพยนตร์", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "แตะเพื่อดูหนังพร้อมกันและแชทสด", color = TextSecondary, fontSize = 11.sp)
                    }
                }
            }
            else -> {
                Surface(
                    color = if (message.isSelf) NeonCyan.copy(alpha = 0.2f) else ObsidianCard,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.widthIn(max = 280.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                        Text(
                            text = message.content,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = message.time,
                            color = TextMuted,
                            fontSize = 10.sp,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }
    }
}
