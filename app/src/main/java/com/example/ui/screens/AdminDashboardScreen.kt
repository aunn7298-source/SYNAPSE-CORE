package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AdminTrafficStats
import com.example.model.DiscordAlert
import com.example.model.ManagedUser
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    stats: AdminTrafficStats,
    users: List<ManagedUser>,
    discordAlerts: List<DiscordAlert>,
    onBack: () -> Unit,
    onToggleBanUser: (String) -> Unit,
    onPromoteUserVip: (String) -> Unit,
    onSendBroadcastAlert: (String) -> Unit,
    onSendTestDiscordAlert: () -> Unit,
    onSelfHealingTest: () -> Unit
) {
    var broadcastMessage by remember { mutableStateOf("") }
    var showBroadcastSuccess by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = ObsidianBlack,
        topBar = {
            Surface(
                color = ObsidianSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "กลับ",
                                tint = Color.White
                            )
                        }
                        Column {
                            Text(
                                text = "👑 PRESIDENT COMMAND CENTER",
                                color = NeonAmber,
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "ศูนย์ควบคุมแอดมิน & วิเคราะห์ข้อมูลเรียลไทม์",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Surface(
                        color = NeonEmerald.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(6.dp).background(NeonEmerald, CircleShape))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Guardian 24/7", color = NeonEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Live Traffic Monitor Cards (Grid 2x2)
            item {
                Text(
                    text = "📊 มอนิเตอร์ทราฟฟิกสด (Live Traffic)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "ผู้ใช้งานออนไลน์สด",
                        value = "${stats.activeUsersCount} คน",
                        icon = Icons.Default.People,
                        color = NeonCyan,
                        modifier = Modifier.weight(1f)
                    )

                    StatCard(
                        title = "คุยสด Gemini Live",
                        value = "${stats.geminiLiveStreamingCount} สาย",
                        icon = Icons.Default.GraphicEq,
                        color = NeonMagenta,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "รายได้รวมกระเป๋าประธาน",
                        value = "฿${stats.totalRevenueBaht}",
                        icon = Icons.Default.MonetizationOn,
                        color = NeonAmber,
                        modifier = Modifier.weight(1f)
                    )

                    StatCard(
                        title = "สถานะเซิร์ฟเวอร์",
                        value = "${stats.serverLatencyMs}ms (Online)",
                        icon = Icons.Default.CloudDone,
                        color = NeonEmerald,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Self-Healing Guardian Status
            item {
                Surface(
                    color = Color(0xFF0F1E24),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NeonCyan, NeonEmerald))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🛡️ ระบบ Self-Healing ออโต้", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("1 วินาที", color = NeonEmerald, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                            Text(
                                text = "เฝ้าระวังแครชและข้อผิดพลาดตลอด 24 ชม. พร้อมกู้คืนอัตโนมัติ",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = onSelfHealingTest,
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("ทดสอบกู้คืน", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Discord Security Webhook Feed
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔔 Discord Security Alerts (#president-security-logs)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    TextButton(onClick = onSendTestDiscordAlert) {
                        Text("ยิงเทส Discord", color = NeonCyan, fontSize = 11.sp)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    discordAlerts.take(3).forEach { alert ->
                        Surface(
                            color = Color(0xFF1B1B26),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(if (alert.severity == "CRITICAL") NeonRed else NeonAmber, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(alert.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(alert.timestamp, color = TextMuted, fontSize = 9.sp)
                                    }
                                    Text(alert.message, color = TextSecondary, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Emergency Broadcast Alert
            item {
                Surface(
                    color = ObsidianCard,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "📢 ส่งข้อความประกาศด่วน (Emergency Broadcast)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = broadcastMessage,
                                onValueChange = { broadcastMessage = it },
                                placeholder = { Text("พิมพ์ข้อความประกาศถึงทุกคน...", color = TextMuted, fontSize = 12.sp) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = ObsidianInput,
                                    unfocusedContainerColor = ObsidianInput,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = NeonAmber,
                                    unfocusedBorderColor = ObsidianCardBorder
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (broadcastMessage.isNotBlank()) {
                                        onSendBroadcastAlert(broadcastMessage)
                                        broadcastMessage = ""
                                        showBroadcastSuccess = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonAmber),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("ส่งประกาศ", color = ObsidianBlack, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        if (showBroadcastSuccess) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("ส่งประกาศด่วนถึงผู้ใช้งาน 1,428 คนเรียบร้อยแล้ว!", color = NeonEmerald, fontSize = 11.sp)
                        }
                    }
                }
            }

            // User Management List
            item {
                Text(
                    text = "👥 จัดการผู้ใช้งาน & แบนสมาชิก (User Management)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            items(users) { user ->
                Surface(
                    color = ObsidianCard,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(user.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = if (user.isBanned) NeonRed.copy(alpha = 0.2f) else NeonCyan.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (user.isBanned) "โดนแบน" else user.role,
                                        color = if (user.isBanned) NeonRed else NeonCyan,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text("${user.discordTag} • เติมเงินสะสม: ฿${user.totalSpentBaht}", color = TextSecondary, fontSize = 11.sp)
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Button(
                                onClick = { onPromoteUserVip(user.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonViolet.copy(alpha = 0.25f)),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("+VIP", color = Color(0xFFD6BBFB), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { onToggleBanUser(user.id) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (user.isBanned) NeonEmerald.copy(alpha = 0.25f) else NeonRed.copy(alpha = 0.25f)
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (user.isBanned) "ปลดแบน" else "แบน",
                                    color = if (user.isBanned) NeonEmerald else NeonRed,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = ObsidianCard,
        shape = RoundedCornerShape(14.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(color.copy(alpha = 0.5f), Color.Transparent))),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, color = TextSecondary, fontSize = 11.sp)
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
        }
    }
}
