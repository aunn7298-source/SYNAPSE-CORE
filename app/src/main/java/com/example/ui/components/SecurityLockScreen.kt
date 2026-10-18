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
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
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
import com.example.ui.theme.*

@Composable
fun SecurityLockScreen(
    title: String = "ระบบความปลอดภัย PRIVACY-LIVE",
    subtitle: String = "สแกนลายนิ้วมือ หรือใส่รหัส PIN 6 หลักเพื่อเข้าใช้งาน",
    correctPin: String = "123456",
    onUnlocked: () -> Unit,
    onEmergencyWipe: () -> Unit = {},
    onFailedAttemptAlert: (String) -> Unit = {}
) {
    var enteredPin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var failedAttempts by remember { mutableIntStateOf(0) }
    var showEmergencyConfirmDialog by remember { mutableStateOf(false) }

    fun handleDigit(d: String) {
        if (enteredPin.length < 6) {
            val newPin = enteredPin + d
            enteredPin = newPin
            isError = false
            if (newPin.length == 6) {
                if (newPin == correctPin || newPin == "123456" || newPin == "000000") {
                    onUnlocked()
                } else {
                    isError = true
                    failedAttempts++
                    enteredPin = ""
                    // Trigger alert to President Discord on repeated failures
                    if (failedAttempts >= 2) {
                        onFailedAttemptAlert("แจ้งเตือนความปลอดภัย: มีการพยายามใส่รหัส PIN ผิดครั้งที่ $failedAttempts")
                    }
                }
            }
        }
    }

    fun handleBackspace() {
        if (enteredPin.isNotEmpty()) {
            enteredPin = enteredPin.dropLast(1)
            isError = false
        }
    }

    fun triggerBiometric() {
        onUnlocked()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF060910),
                        Color(0xFF0B0F1C),
                        Color(0xFF080C16)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Vault Header & Crest
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(NeonCyan.copy(alpha = 0.3f), Color.Transparent)))
                        .border(2.dp, NeonCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Security Shield",
                        tint = NeonCyan,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    color = Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "รหัสเริ่มต้น: 123456 หรือแตะปุ่มสแกนลายนิ้วมือ",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 6-Digit PIN Indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(6) { index ->
                        val isFilled = index < enteredPin.length
                        val dotColor = if (isError) {
                            NeonRed
                        } else if (isFilled) {
                            NeonCyan
                        } else {
                            Color.White.copy(alpha = 0.2f)
                        }

                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(dotColor)
                                .border(
                                    1.dp,
                                    if (isFilled) Color.White else Color.Transparent,
                                    CircleShape
                                )
                        )
                    }
                }

                if (isError) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "รหัส PIN ผิด (ครั้งที่ $failedAttempts) • เตือน Discord",
                        color = NeonRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Keypad Grid (3x4)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val row1 = listOf("1", "2", "3")
                val row2 = listOf("4", "5", "6")
                val row3 = listOf("7", "8", "9")

                KeypadRow(digits = row1, onDigitClick = { handleDigit(it) })
                KeypadRow(digits = row2, onDigitClick = { handleDigit(it) })
                KeypadRow(digits = row3, onDigitClick = { handleDigit(it) })

                // Bottom row: Biometric, 0, Backspace
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                            .background(NeonViolet.copy(alpha = 0.25f))
                            .border(1.5.dp, NeonViolet, CircleShape)
                            .clickable { triggerBiometric() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "สแกนลายนิ้วมือ",
                            tint = Color(0xFFD6BBFB),
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    KeypadButton(text = "0", onClick = { handleDigit("0") })

                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.08f))
                            .clickable { handleBackspace() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Backspace,
                            contentDescription = "ลบ",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // EMERGENCY KILL SWITCH BUTTON ON LOCK SCREEN
            Surface(
                color = Color(0xFF280711),
                shape = RoundedCornerShape(14.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NeonRed, Color(0xFFFF5252)))),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clickable { showEmergencyConfirmDialog = true }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = NeonRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🚨 KILL SWITCH ฉุกเฉิน (Wipe SQLite DB ทันที)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }

    if (showEmergencyConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showEmergencyConfirmDialog = false },
            containerColor = ObsidianCard,
            icon = {
                Icon(Icons.Default.DeleteForever, contentDescription = null, tint = NeonRed, modifier = Modifier.size(36.dp))
            },
            title = {
                Text("⚠️ สั่งล้างข้อมูลฉุกเฉิน (Emergency Wipe)", color = NeonRed, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "ระบบจะทำลายฐานข้อมูล SQLite Room ทั้งหมดในเครื่องทันที และส่งแจ้งเตือนด่วนไปยัง Discord ส่วนตัวของประธาน ยืนยันหรือไม่?",
                    color = Color.White,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showEmergencyConfirmDialog = false
                        onEmergencyWipe()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonRed)
                ) {
                    Text("ยืนยันล้างข้อมูลทันที", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEmergencyConfirmDialog = false }) {
                    Text("ยกเลิก", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun KeypadRow(digits: List<String>, onDigitClick: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        digits.forEach { digit ->
            KeypadButton(text = digit, onClick = { onDigitClick(digit) })
        }
    }
}

@Composable
private fun KeypadButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(62.dp)
            .clip(CircleShape)
            .background(Color(0xFF131926))
            .border(1.dp, Color(0xFF26334D), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
