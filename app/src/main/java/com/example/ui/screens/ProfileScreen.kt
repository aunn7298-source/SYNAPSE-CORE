package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    onTriggerKillSwitch: () -> Unit,
    onLockAppNow: () -> Unit,
    onOpenSocialFi: () -> Unit = {},
    onOpenAdminDashboard: () -> Unit = {}
) {
    var isBiometricEnabled by remember { mutableStateOf(true) }
    var isAppLockEnabled by remember { mutableStateOf(true) }
    var isAntiScreenCaptureEnabled by remember { mutableStateOf(true) }
    var showKillSwitchDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
    ) {
        item {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "โปรไฟล์ & ศูนย์ความปลอดภัย",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                IconButton(
                    onClick = onLockAppNow,
                    modifier = Modifier.background(NeonCyan.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "ล็อคแอปทันที",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Profile Avatar Card
        item {
            Surface(
                color = ObsidianCard,
                shape = RoundedCornerShape(20.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonCyan, NeonViolet))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(NeonCyan, NeonViolet))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "คุณ",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PRIVACY VIP USER",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "@privacy_agent_007",
                            color = NeonCyan,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            color = NeonEmerald.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "🛡️ การป้องกันความปลอดภัยระดับสูง",
                                color = NeonEmerald,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(14.dp)) }

        // Action Buttons: Social-Fi & Admin Command Center
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    color = Color(0xFF1E1430),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonViolet, NeonMagenta))),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenSocialFi() }
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = NeonMagenta, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("กระเป๋าเงิน Social-Fi", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("เติมเงิน & กาชา", color = TextSecondary, fontSize = 10.sp)
                    }
                }

                Surface(
                    color = Color(0xFF281E0C),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonAmber, Color(0xFFFF8C00)))),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenAdminDashboard() }
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = NeonAmber, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("แอดมินสำหรับประธาน", color = NeonAmber, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Traffic & รายได้สด", color = TextSecondary, fontSize = 10.sp)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        // Section: Security & Biometrics
        item {
            Text(
                text = "การยืนยันตัวตน & รหัสผ่าน (Biometric & PIN)",
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    // Biometric Switch
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null, tint = NeonCyan)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("สแกนลายนิ้วมือ (Biometric)", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Text("ใช้ BiometricPrompt ก่อนเปิดดูแชท", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                        Switch(
                            checked = isBiometricEnabled,
                            onCheckedChange = { isBiometricEnabled = it }
                        )
                    }

                    HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                    // 6-digit PIN Switch
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Pin, contentDescription = null, tint = NeonViolet)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("รหัสผ่าน PIN 6 หลัก", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Text("กำหนดรหัส PIN ป้องกันการเข้าถึง (ค่าเริ่มต้น: 123456)", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                        Switch(
                            checked = isAppLockEnabled,
                            onCheckedChange = { isAppLockEnabled = it }
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        // Section: Room Database Storage Stats
        item {
            Text(
                text = "ฐานข้อมูลความปลอดภัยในเครื่อง (Local Room DB)",
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("ประวัติแชทที่เข้ารหัสใน SQLite Room", color = Color.White, fontSize = 13.sp)
                        Text("ปลอดภัย 100%", color = NeonEmerald, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ข้อมูลทั้งหมดไม่ถูกส่งไปยังเซิร์ฟเวอร์ภายนอก บันทึกลงในฐานข้อมูล Room ภายในเครื่องเท่านั้น เพื่อความเป็นส่วนตัวสูงสุด",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        // Section: EMERGENCY KILL SWITCH (Wipe All Data)
        item {
            Surface(
                color = Color(0xFF2E0914),
                shape = RoundedCornerShape(18.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NeonRed, Color(0xFFFF6B6B)))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(NeonRed, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "ระบบฉุกเฉิน KILL SWITCH",
                                color = NeonRed,
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "ล้างข้อมูลประวัติและแชททั้งหมดทันที",
                                color = Color.White,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "กรณีมือถือตกอยู่ในความเสี่ยงหรือต้องการลบร่องรอยทั้งหมด ปุ่มนี้จะทำลายประวัติการโทร ข้อความเสียง และแชทลับใน Room Database ทันทีแบบกู้คืนไม่ได้",
                        color = Color(0xFFFFB4AB),
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { showKillSwitchDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ทำลายข้อมูลทั้งหมดทันที (Emergency Wipe)",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(100.dp)) }
    }

    // Confirmation Dialog for Kill Switch
    if (showKillSwitchDialog) {
        AlertDialog(
            onDismissRequest = { showKillSwitchDialog = false },
            containerColor = ObsidianCard,
            title = {
                Text(
                    text = "⚠️ ยืนยันการใช้งาน Kill Switch",
                    color = NeonRed,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "คุณแน่ใจหรือไม่ว่าต้องการล้างข้อมูลแชท ประวัติการโทร และบันทึกทั้งหมดอย่างถาวร? การกระทำนี้ไม่สามารถย้อนกลับได้",
                    color = Color.White
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showKillSwitchDialog = false
                        onTriggerKillSwitch()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonRed)
                ) {
                    Text("ล้างข้อมูลทันที (Wipe)", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showKillSwitchDialog = false }) {
                    Text("ยกเลิก", color = TextSecondary)
                }
            }
        )
    }
}
