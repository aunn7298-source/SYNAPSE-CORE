package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
import com.example.ui.theme.*

@Composable
fun VoiceNoteBubble(
    message: ChatMessage,
    onTogglePlay: () -> Unit
) {
    var speed by remember { mutableStateOf("1.0x") }

    val infiniteTransition = rememberInfiniteTransition(label = "waveAnim")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveScale"
    )

    Surface(
        color = if (message.isSelf) ObsidianCard else Color(0xFF1E283D),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        modifier = Modifier
            .widthIn(min = 220.dp, max = 290.dp)
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Encrypted tag
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "E2EE",
                    tint = NeonCyan,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ข้อความเสียงเข้ารหัสส่วนตัว E2EE",
                    fontSize = 10.sp,
                    color = NeonCyan,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Play / Pause Button
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (message.isPlayingVoice) NeonMagenta else NeonCyan)
                        .clickable { onTogglePlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (message.isPlayingVoice) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "เล่นข้อความเสียง",
                        tint = ObsidianBlack,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Waveform Bars Visualizer
                val amplitudes = remember(message.id) {
                    if (message.voiceWaveformAmplitudes.isNotEmpty()) {
                        message.voiceWaveformAmplitudes
                    } else {
                        listOf(0.3f, 0.7f, 0.9f, 0.4f, 0.8f, 1.0f, 0.6f, 0.5f, 0.8f, 0.4f, 0.9f, 0.7f)
                    }
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.5.dp)
                ) {
                    amplitudes.forEachIndexed { index, amp ->
                        val barProgress = index.toFloat() / amplitudes.size
                        val isPlayed = barProgress <= message.voiceCurrentProgress
                        val heightMultiplier = if (message.isPlayingVoice && isPlayed) waveScale else 1f
                        val barHeight = (amp * 26f * heightMultiplier).coerceIn(4f, 28f)

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(barHeight.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (isPlayed) NeonCyan else Color.White.copy(alpha = 0.3f)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Speed button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                        .clickable {
                            speed = when (speed) {
                                "1.0x" -> "1.5x"
                                "1.5x" -> "2.0x"
                                else -> "1.0x"
                            }
                        }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = speed,
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Duration & Time
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val durSec = message.voiceDurationSeconds
                val displayTime = String.format("%02d:%02d", durSec / 60, durSec % 60)
                Text(
                    text = displayTime,
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                Text(
                    text = message.time,
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }
        }
    }
}
