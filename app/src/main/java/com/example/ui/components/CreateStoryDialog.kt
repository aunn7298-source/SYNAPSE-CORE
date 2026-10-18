package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun CreateStoryDialog(
    onDismiss: () -> Unit,
    onPostStory: (caption: String, emoji: String, music: String?) -> Unit
) {
    var caption by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("🔥") }
    var selectedMusic by remember { mutableStateOf("Synthwave Pulse 2099") }
    var selectedGradientIndex by remember { mutableIntStateOf(0) }

    val gradientPresets = listOf(
        listOf(0xFF00F2FE, 0xFF4FACFE),
        listOf(0xFF8A2BE2, 0xFFFF007F),
        listOf(0xFF1F1C2C, 0xFF928DAB),
        listOf(0xFFF12711, 0xFFF5AF19),
        listOf(0xFF11998E, 0xFF38EF7D)
    )

    val currentBrush = remember(selectedGradientIndex) {
        Brush.verticalGradient(gradientPresets[selectedGradientIndex].map { Color(it) })
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = ObsidianCard,
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonCyan, NeonViolet))),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✨ ลงสตอรี่ของคุณ (Create Story)",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "ปิด",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Live Preview Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(currentBrush)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = selectedEmoji, fontSize = 44.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (caption.isBlank()) "พิมพ์ข้อความสตอรี่ของคุณที่นี่..." else caption,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            maxLines = 3
                        )
                    }

                    // Music Tag
                    Surface(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.align(Alignment.BottomStart)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = selectedMusic, color = Color.White, fontSize = 10.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Caption Input
                OutlinedTextField(
                    value = caption,
                    onValueChange = { caption = it },
                    placeholder = { Text("ใส่ข้อความสตอรี่ (กำลังดูหนัง, คุยกับเพื่อน)...", color = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ObsidianInput,
                        unfocusedContainerColor = ObsidianInput,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = ObsidianCardBorder
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Choose Emoji Sticker
                Text(text = "เลือกสติกเกอร์ (Emoji Sticker)", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    val emojis = listOf("🔥", "🍿", "🎬", "⚡", "🎮", "🌌", "❤️", "🎙️", "✨")
                    items(emojis) { emoji ->
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(if (selectedEmoji == emoji) NeonViolet.copy(alpha = 0.4f) else ObsidianInput)
                                .border(
                                    1.dp,
                                    if (selectedEmoji == emoji) NeonViolet else Color.Transparent,
                                    CircleShape
                                )
                                .clickable { selectedEmoji = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 20.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Choose Gradient Color
                Text(text = "เลือกโทนสีพื้นหลัง (Background Theme)", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(gradientPresets.indices.toList()) { idx ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(gradientPresets[idx].map { Color(it) }))
                                .border(
                                    2.dp,
                                    if (selectedGradientIndex == idx) Color.White else Color.Transparent,
                                    CircleShape
                                )
                                .clickable { selectedGradientIndex = idx }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Post Button
                Button(
                    onClick = {
                        val text = if (caption.isBlank()) "กำลังสนุกใน PRIVACY-LIVE 🍿✨" else caption
                        onPostStory(text, selectedEmoji, selectedMusic)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "โพสต์ลงสตอรี่ทันที (Post Story)",
                        color = ObsidianBlack,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
