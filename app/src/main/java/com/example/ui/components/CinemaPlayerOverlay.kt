package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Movie
import com.example.model.WatchReaction
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CinemaPlayerOverlay(
    movie: Movie,
    isPlaying: Boolean,
    playbackSeconds: Int,
    isWatchParty: Boolean,
    watchRoom: com.example.model.WatchRoom? = null,
    reactions: List<WatchReaction>,
    selectedQuality: String,
    selectedSubtitle: String,
    selectedAudio: String,
    onPlayPause: () -> Unit,
    onRewind: () -> Unit,
    onForward: () -> Unit,
    onSeek: (Int) -> Unit,
    onSendReaction: (String) -> Unit,
    onSendRoomMessage: (String) -> Unit = {},
    onClose: () -> Unit,
    onSelectQuality: (String) -> Unit,
    onSelectSubtitle: (String) -> Unit,
    onSelectAudio: (String) -> Unit
) {
    var showControls by remember { mutableStateOf(true) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showRoomChatSheet by remember { mutableStateOf(false) }
    var chatInputText by remember { mutableStateOf("") }
    val context = LocalContext.current

    val totalSec = movie.totalSeconds
    val progress = if (totalSec > 0) playbackSeconds.toFloat() / totalSec else 0f

    val resId = remember(movie.posterDrawableName) {
        val id = context.resources.getIdentifier(movie.posterDrawableName, "drawable", context.packageName)
        if (id != 0) id else com.example.R.drawable.ic_launcher_background
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { showControls = !showControls }
    ) {
        // Video backdrop simulation
        Image(
            painter = painterResource(id = resId),
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Cinema ambient vignette
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.85f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.95f)
                        )
                    )
                )
        )

        // Floating watch party reactions
        Box(modifier = Modifier.fillMaxSize()) {
            reactions.forEach { reaction ->
                key(reaction.id) {
                    FloatingReactionItem(reaction = reaction)
                }
            }
        }

        // Live Watch Party badge
        if (isWatchParty) {
            Surface(
                color = NeonViolet.copy(alpha = 0.85f),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 48.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(NeonCyan, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "WATCH PARTY • ดูพร้อมกัน ${movie.currentWatchers} คน",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Player Controls Overlay
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Top Action Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "กลับ",
                            tint = Color.White
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
                    ) {
                        Text(
                            text = movie.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "$selectedQuality • $selectedAudio",
                            color = NeonCyan,
                            fontSize = 11.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { showRoomChatSheet = true },
                            modifier = Modifier
                                .background(NeonViolet.copy(alpha = 0.6f), CircleShape)
                        ) {
                            BadgedBox(badge = {
                                val count = watchRoom?.chatMessages?.size ?: 0
                                if (count > 0) {
                                    Badge(containerColor = NeonMagenta) {
                                        Text("$count")
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = "แชทระหว่างดูหนัง",
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { showSettingsSheet = true },
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "การตั้งค่าสตรีม",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Center Play / Rewind / Fast-Forward controls
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(28.dp)
                ) {
                    IconButton(
                        onClick = onRewind,
                        modifier = Modifier
                            .size(52.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay10,
                            contentDescription = "ย้อนหลัง 10 วิ",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    IconButton(
                        onClick = onPlayPause,
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                Brush.linearGradient(listOf(NeonCyan, NeonCyanGlow)),
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "หยุดชั่วคราว" else "เล่นหนัง",
                            tint = ObsidianBlack,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    IconButton(
                        onClick = onForward,
                        modifier = Modifier
                            .size(52.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forward10,
                            contentDescription = "ไปข้างหน้า 10 วิ",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Bottom Timeline and Reaction Bar
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Quick Watch Party Reaction buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("🍿", "❤️", "🔥", "😱", "👏", "⚡").forEach { emoji ->
                            Surface(
                                color = Color.White.copy(alpha = 0.12f),
                                shape = CircleShape,
                                modifier = Modifier
                                    .size(42.dp)
                                    .clickable { onSendReaction(emoji) }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = emoji, fontSize = 20.sp)
                                }
                            }
                        }
                    }

                    // Progress Slider
                    Slider(
                        value = progress,
                        onValueChange = { newProg ->
                            onSeek((newProg * totalSec).toInt())
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = NeonCyan,
                            activeTrackColor = NeonCyan,
                            inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Timestamps & Quality row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatTime(playbackSeconds),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "ซับไตเติล: $selectedSubtitle",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )

                        Text(
                            text = formatTime(totalSec),
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }

    if (showSettingsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSettingsSheet = false },
            containerColor = ObsidianCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "ตั้งค่าการเล่นภาพยนตร์",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(text = "ความละเอียดวิดีโอ (Quality)", color = NeonCyan, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("4K Ultra HDR", "1080p 60fps", "720p HD").forEach { q ->
                        FilterChip(
                            selected = selectedQuality == q,
                            onClick = { onSelectQuality(q) },
                            label = { Text(q) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "ซับไตเติล (Subtitles)", color = NeonCyan, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("ภาษาไทย (TH)", "English (EN)", "ปิด").forEach { s ->
                        FilterChip(
                            selected = selectedSubtitle == s,
                            onClick = { onSelectSubtitle(s) },
                            label = { Text(s) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "ระบบเสียง (Audio Track)", color = NeonCyan, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("เสียงไทย 5.1 Dolby Atmos", "English Original Atmos 7.1").forEach { a ->
                        FilterChip(
                            selected = selectedAudio == a,
                            onClick = { onSelectAudio(a) },
                            label = { Text(a) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showRoomChatSheet) {
        ModalBottomSheet(
            onDismissRequest = { showRoomChatSheet = false },
            containerColor = ObsidianCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .navigationBarsPadding()
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "💬 แชทห้องดูหนัง (Watch Room Chat)",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "รหัสห้อง: ${watchRoom?.roomCode ?: "PV-8821"} • ซิงค์ตรงกัน 100%",
                            color = NeonCyan,
                            fontSize = 11.sp
                        )
                    }

                    Surface(
                        color = NeonViolet.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "${watchRoom?.participants?.size ?: 3} คนในห้อง",
                            color = Color(0xFFD6BBFB),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Messages List
                val messages = watchRoom?.chatMessages ?: listOf(
                    com.example.model.RoomChatMessage("1", "ระบบ", "ห้องส่วนตัวพร้อมแล้ว", "14:40", true),
                    com.example.model.RoomChatMessage("2", "แพรว Ploy", "หนังเรื่องนี้มันส์มากก 🍿", "14:41")
                )

                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(messages.size) { idx ->
                        val msg = messages[idx]
                        if (msg.isSystemSyncNotice) {
                            Surface(
                                color = Color.White.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = msg.message,
                                    color = NeonCyan,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        } else {
                            val isMe = msg.senderName.contains("คุณ")
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                            ) {
                                Text(
                                    text = msg.senderName,
                                    color = if (isMe) NeonCyan else NeonViolet,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    color = if (isMe) NeonCyan.copy(alpha = 0.2f) else Color(0xFF1E283D),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.widthIn(max = 260.dp)
                                ) {
                                    Text(
                                        text = msg.message,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Chat Input Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = chatInputText,
                        onValueChange = { chatInputText = it },
                        placeholder = { Text("คุยกับเพื่อนขณะดูหนัง...", color = TextMuted, fontSize = 13.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianInput,
                            unfocusedContainerColor = ObsidianInput,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = ObsidianCardBorder
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (chatInputText.isNotBlank()) {
                                onSendRoomMessage(chatInputText)
                                chatInputText = ""
                            }
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .background(NeonCyan, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "ส่งข้อความในห้อง",
                            tint = ObsidianBlack,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun FloatingReactionItem(reaction: WatchReaction) {
    val animOffsetY = remember { Animatable(0f) }
    val animAlpha = remember { Animatable(1f) }

    LaunchedEffect(reaction.id) {
        animOffsetY.animateTo(
            targetValue = -350f,
            animationSpec = tween(durationMillis = 2200, easing = LinearOutSlowInEasing)
        )
    }
    LaunchedEffect(reaction.id) {
        delay(1200)
        animAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 120.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(
            text = reaction.emoji,
            fontSize = 32.sp,
            modifier = Modifier
                .offset(x = (reaction.xOffset * 300).dp, y = animOffsetY.value.dp)
        )
    }
}

private fun formatTime(seconds: Int): String {
    val hrs = seconds / 3600
    val mins = (seconds % 3600) / 60
    val secs = seconds % 60
    return if (hrs > 0) {
        String.format("%02d:%02d:%02d", hrs, mins, secs)
    } else {
        String.format("%02d:%02d", mins, secs)
    }
}
