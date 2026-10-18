package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Videocam
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
fun PostClipDialog(
    onDismiss: () -> Unit,
    onPostClip: (title: String, tags: List<String>, privacy: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var tagsInput by remember { mutableStateOf("#หนังใหม่ #privacylive #ไซไฟ") }
    var selectedPrivacy by remember { mutableStateOf("สาธารณะ") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = ObsidianCard,
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonMagenta, NeonViolet))),
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            tint = NeonMagenta,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "โพสต์คลิปวิดีโอ (Post Clip)",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "ปิด", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title Input
                Text(text = "คำอธิบายคลิป (Caption)", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("เช่น ฉากแอ็กชันสุดมันส์, รีวิวหนัง, เบื้องหลัง...", color = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ObsidianInput,
                        unfocusedContainerColor = ObsidianInput,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = NeonMagenta,
                        unfocusedBorderColor = ObsidianCardBorder
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tags Input
                Text(text = "แท็ก (Hashtags)", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = tagsInput,
                    onValueChange = { tagsInput = it },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ObsidianInput,
                        unfocusedContainerColor = ObsidianInput,
                        focusedTextColor = NeonCyan,
                        unfocusedTextColor = NeonCyan,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = ObsidianCardBorder
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Privacy Selector
                Text(text = "ความเป็นส่วนตัวของคลิป", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("สาธารณะ", "เพื่อนเท่านั้น", "ส่วนตัว").forEach { priv ->
                        val isSelected = selectedPrivacy == priv
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedPrivacy = priv },
                            label = { Text(priv) },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (priv == "สาธารณะ") Icons.Default.Public else Icons.Default.Lock,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val finalTitle = if (title.isBlank()) "คลิปใหม่จาก PRIVACY-LIVE 🎬🔥" else title
                        val tags = tagsInput.split(" ").filter { it.isNotBlank() }
                        onPostClip(finalTitle, tags, selectedPrivacy)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonMagenta),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "เผยแพร่คลิปทันที (Publish Clip)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
