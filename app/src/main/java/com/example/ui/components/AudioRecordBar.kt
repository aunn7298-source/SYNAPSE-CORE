package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AudioRecordBar(
    durationSeconds: Int,
    onCancel: () -> Unit,
    onSend: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "recPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Surface(
        color = Color(0xFF1B0B14),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cancel button
            IconButton(
                onClick = onCancel,
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "ยกเลิกบันทึก",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Pulsing recording dot
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(NeonRed.copy(alpha = pulseAlpha), CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Timer
            val mins = durationSeconds / 60
            val secs = durationSeconds % 60
            Text(
                text = String.format("%02d:%02d", mins, secs),
                color = NeonRed,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Simulated live mic amplitude wave
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                listOf(0.4f, 0.8f, 1.0f, 0.6f, 0.9f, 0.5f, 0.7f, 0.3f, 0.9f, 0.6f).forEachIndexed { index, amp ->
                    val dynamicH = if (index % 2 == 0) amp * pulseAlpha else amp * (1.3f - pulseAlpha)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height((dynamicH * 18).coerceIn(4f, 20f).dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(NeonRed.copy(alpha = 0.8f))
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Send voice button
            IconButton(
                onClick = onSend,
                modifier = Modifier
                    .size(42.dp)
                    .background(NeonCyan, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "ส่งข้อความเสียง",
                    tint = ObsidianBlack,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
