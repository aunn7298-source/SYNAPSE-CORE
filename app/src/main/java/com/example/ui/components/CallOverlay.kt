package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActiveCall
import com.example.model.CallType
import com.example.ui.theme.*

@Composable
fun CallOverlay(
    call: ActiveCall,
    onEndCall: () -> Unit,
    onToggleMute: () -> Unit,
    onToggleSpeaker: () -> Unit,
    onToggleVideo: () -> Unit,
    onToggleCameraSwitch: () -> Unit,
    onToggleScreenShare: () -> Unit,
    onToggleMinimize: () -> Unit
) {
    val mins = call.durationSeconds / 60
    val secs = call.durationSeconds % 60
    val timeFormatted = String.format("%02d:%02d", mins, secs)

    val infiniteTransition = rememberInfiniteTransition(label = "pulseRing")
    val pulseBorder by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseBorder"
    )

    // Floating PIP when minimized
    if (call.isMinimized) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 16.dp, bottom = 100.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Surface(
                color = ObsidianCard,
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonCyan, NeonViolet))),
                tonalElevation = 8.dp,
                modifier = Modifier
                    .size(130.dp, 160.dp)
                    .clickable { onToggleMinimize() }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(NeonViolet),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = call.contactName.take(1),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = call.contactName,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = timeFormatted,
                            color = NeonCyan,
                            fontSize = 10.sp
                        )
                    }

                    // Quick end button in PIP
                    IconButton(
                        onClick = onEndCall,
                        modifier = Modifier
                            .size(26.dp)
                            .align(Alignment.TopEnd)
                            .padding(2.dp)
                            .background(NeonRed, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "วางสาย",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
        return
    }

    // FULL SCREEN VIDEO / VOICE CALL VIEW
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070A10))
    ) {
        // VIDEO CALL BACKGROUND FEED
        if (call.callType == CallType.VIDEO && call.isVideoEnabled && !call.isScreenSharing) {
            // Simulated High Definition Remote Video Feed
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF131A2A),
                                Color(0xFF1E283D),
                                Color(0xFF0D121D)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(NeonViolet, NeonMagenta))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = call.contactName.take(1),
                            color = Color.White,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "วิดีโอคอล HD • 1080p 60fps",
                        color = NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (call.isFrontCamera) "กำลังใช้กล้องหน้า (Front Camera)" else "กำลังใช้กล้องหลัง (Back Camera)",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        } else if (call.isScreenSharing) {
            // SCREEN SHARING MODE
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(top = 56.dp, bottom = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = Color(0xFF003840),
                    shape = RoundedCornerShape(24.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NeonCyan, NeonCyanGlow))),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(NeonRed, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "🔴 สตรีมแชร์หน้าจอสด (Screen Sharing Active)",
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "1080p 60fps",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                // Shared Screen Window Frame
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF101726))
                        .border(2.dp, NeonCyan, RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PRIVACY-LIVE SCREEN DISPLAY",
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "🔒 E2EE 256-bit",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF1E1035), Color(0xFF0F2027), Color(0xFF2C5364))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.ScreenShare,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(54.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "กำลังแชร์หน้าจอกับ ${call.contactName}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "ผู้เข้าร่วมสายจะเห็นภาพยนตร์หรือแอปพลิเคชันที่คุณกำลังเปิดแบบเรียลไทม์",
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = onToggleScreenShare,
                            colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.StopScreenShare, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("หยุดแชร์หน้าจอ", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // VOICE CALL / VIDEO DISABLED VIEW
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(top = 60.dp, bottom = 120.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dynamic Connection State Badge
                val stateColor = Color(call.connectionState.statusColorHex)
                Surface(
                    color = stateColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(stateColor, stateColor.copy(alpha = 0.4f)))),
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(stateColor, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = call.connectionState.labelThai,
                            color = stateColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• $timeFormatted",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Dynamic Audio Waveform Indicator during call
                Row(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    listOf(0.4f, 0.8f, 1.0f, 0.6f, 0.9f, 0.7f, 0.5f, 0.9f, 0.6f).forEach { amp ->
                        val barH = (amp * call.audioWaveIntensity * 20f).coerceIn(4f, 22f)
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(barH.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(stateColor)
                        )
                    }
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(170.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .border(
                                width = (pulseBorder * 2).dp,
                                color = stateColor.copy(alpha = (1.5f - pulseBorder * 0.4f).coerceIn(0.1f, 0.8f)),
                                shape = CircleShape
                            )
                    )

                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(stateColor, NeonViolet))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = call.contactName.take(1),
                            color = Color.White,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = call.contactName,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = call.connectionState.subText,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                if (!call.isVideoEnabled && call.callType == CallType.VIDEO) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "(ปิดกล้องวิดีโออยู่ แตะปุ่มกล้องเพื่อเปิด)",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // TOP HEADER BAR (Minimize & Encryption Status)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onToggleMinimize,
                modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.CloseFullscreen,
                    contentDescription = "ย่อหน้าจอคอล (PIP)",
                    tint = Color.White
                )
            }

            Surface(
                color = Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (call.callType == CallType.VIDEO) Icons.Default.Videocam else Icons.Default.Call,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = timeFormatted, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Camera Switch button if video call
            if (call.callType == CallType.VIDEO && call.isVideoEnabled) {
                IconButton(
                    onClick = onToggleCameraSwitch,
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.FlipCameraAndroid,
                        contentDescription = "สลับกล้องหน้า/หลัง",
                        tint = Color.White
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }
        }

        // BOTTOM CONTROLS DOCK
        Surface(
            color = Color(0xF2121826),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Audio Mute/Unmute
                    CallDockButton(
                        icon = if (call.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        label = if (call.isMuted) "ไมค์ปิด" else "ไมค์เปิด",
                        isActive = call.isMuted,
                        activeColor = NeonRed,
                        onClick = onToggleMute
                    )

                    // Video Camera On/Off
                    CallDockButton(
                        icon = if (call.isVideoEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
                        label = if (call.isVideoEnabled) "กล้องเปิด" else "กล้องปิด",
                        isActive = !call.isVideoEnabled,
                        activeColor = NeonRed,
                        onClick = onToggleVideo
                    )

                    // Speakerphone Toggle
                    CallDockButton(
                        icon = if (call.isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        label = if (call.isSpeakerOn) "ลำโพง" else "หูฟัง",
                        isActive = call.isSpeakerOn,
                        activeColor = NeonCyan,
                        onClick = onToggleSpeaker
                    )

                    // Screen Share Toggle
                    CallDockButton(
                        icon = if (call.isScreenSharing) Icons.Default.StopScreenShare else Icons.Default.ScreenShare,
                        label = if (call.isScreenSharing) "หยุดแชร์" else "แชร์หน้าจอ",
                        isActive = call.isScreenSharing,
                        activeColor = NeonCyan,
                        onClick = onToggleScreenShare
                    )

                    // End Call
                    IconButton(
                        onClick = onEndCall,
                        modifier = Modifier
                            .size(52.dp)
                            .background(NeonRed, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CallEnd,
                            contentDescription = "วางสาย",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CallDockButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (isActive) activeColor else Color.White.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) ObsidianBlack else Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isActive) activeColor else TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
