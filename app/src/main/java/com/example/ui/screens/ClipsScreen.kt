package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClipComment
import com.example.model.ClipItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClipsScreen(
    clips: List<ClipItem>,
    commentsMap: Map<String, List<ClipComment>>,
    activeCommentsClipId: String?,
    onToggleLike: (String) -> Unit,
    onOpenComments: (String) -> Unit,
    onCloseComments: () -> Unit,
    onAddComment: (String, String) -> Unit,
    onOpenPostDialog: () -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    val currentClip = clips.getOrNull(currentIndex) ?: clips.firstOrNull()

    // Disc rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "discRotate")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(clips.size) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < -30) {
                        // Drag up -> Next clip
                        if (currentIndex < clips.size - 1) currentIndex++
                    } else if (dragAmount > 30) {
                        // Drag down -> Previous clip
                        if (currentIndex > 0) currentIndex--
                    }
                }
            }
    ) {
        currentClip?.let { clip ->
            // Clip simulated video canvas with gradient and dynamic particle lines
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            clip.gradientColors.map { Color(it) }
                        )
                    )
            ) {
                // Ambient vignette
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.5f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                // Top Header (Clips indicator & Post button)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "คลิปสั้น PRIVACY",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = NeonMagenta.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "HD REELS",
                                color = NeonMagenta,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Post Clip Button
                    Button(
                        onClick = onOpenPostDialog,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonMagenta),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("โพสต์คลิป", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Center visual elements (Neon play glow)
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.04f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.size(70.dp)
                    )
                }

                // Right Action Sidebar (Like, Comment, Share, Sound Disc)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Creator avatar
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(NeonViolet, NeonMagenta)))
                            .border(1.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = clip.creatorName.take(1),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Like Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onToggleLike(clip.id) }
                    ) {
                        Icon(
                            imageVector = if (clip.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "ถูกใจ",
                            tint = if (clip.isLiked) NeonMagenta else Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${clip.likesCount}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Comment Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onOpenComments(clip.id) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "ความคิดเห็น",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${clip.commentsCount}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Share Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { /* quick share */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "แชร์ไปยังแชท",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${clip.sharesCount}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Spinning Vinyl Audio Disc
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .rotate(rotation)
                            .clip(CircleShape)
                            .background(Color(0xFF1B1B1B))
                            .border(2.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Bottom Clip Meta (Creator, Title, Tags, Soundtrack)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth(0.80f)
                        .padding(start = 16.dp, bottom = 100.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = clip.creatorName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = clip.creatorHandle,
                            color = NeonCyan,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = clip.title,
                        color = Color.White,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        clip.tags.take(3).forEach { tag ->
                            Text(
                                text = tag,
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = clip.soundTrack,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }

    // Comments Bottom Sheet
    if (activeCommentsClipId != null) {
        val comments = commentsMap[activeCommentsClipId] ?: emptyList()
        var newCommentText by remember { mutableStateOf("") }

        ModalBottomSheet(
            onDismissRequest = onCloseComments,
            containerColor = ObsidianCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .navigationBarsPadding()
            ) {
                Text(
                    text = "ความคิดเห็น (${comments.size})",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(comments) { comment ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(NeonViolet),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = comment.author.take(1),
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = comment.author,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = comment.timeAgo,
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                }
                                Text(
                                    text = comment.text,
                                    color = TextPrimary,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Comment input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newCommentText,
                        onValueChange = { newCommentText = it },
                        placeholder = { Text("เพิ่มความคิดเห็น...", color = TextMuted, fontSize = 13.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianInput,
                            unfocusedContainerColor = ObsidianInput,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = NeonMagenta,
                            unfocusedBorderColor = ObsidianCardBorder
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (newCommentText.isNotBlank()) {
                                onAddComment(activeCommentsClipId, newCommentText)
                                newCommentText = ""
                            }
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .background(NeonMagenta, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "ส่งความคิดเห็น",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
