package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GachaItem
import com.example.model.GachaRarity
import com.example.model.PaymentPackage
import com.example.model.VipTier
import com.example.ui.theme.*

@Composable
fun SocialFiScreen(
    balanceCoins: Int,
    balanceGems: Int,
    vipTier: VipTier,
    linkedDiscordTag: String,
    onTopUpSuccess: (Int, Int, VipTier) -> Unit,
    onPlayGacha: (Int) -> GachaItem?
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Wallet/Top-up, 1: Gacha, 2: Shop
    var showPaymentModalForPackage by remember { mutableStateOf<PaymentPackage?>(null) }
    var wonGachaItem by remember { mutableStateOf<GachaItem?>(null) }
    var isSpinningGacha by remember { mutableStateOf(false) }

    val packages = listOf(
        PaymentPackage("pkg_1", "Starter VIP Pack", 59, 500, 50, "ปลดล็อค Gemini Live 7 วัน + ยศ Discord VIP"),
        PaymentPackage("pkg_2", "Cinema Pro Pass", 149, 1500, 200, "สตรีม 4K Watch Party ไม่จำกัด 30 วัน"),
        PaymentPackage("pkg_3", "👑 PRESIDENT VIP PACK", 399, 5000, 1000, "ยศประธาน Discord ถาวร + สุ่มกาชาฟรี 5 ครั้ง")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(bottom = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "กระเป๋าเงิน & เศรษฐกิจ (Social-Fi)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "เชื่อมโยงบัญชี Discord & ระบบรายได้อัตโนมัติ",
                        color = NeonCyan,
                        fontSize = 11.sp
                    )
                }

                Surface(
                    color = Color(vipTier.badgeColorHex).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(vipTier.badgeColorHex), Color.White)))
                ) {
                    Text(
                        text = vipTier.labelThai,
                        color = Color(vipTier.badgeColorHex),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Cross-Platform Wallet Card
        item {
            Surface(
                color = ObsidianCard,
                shape = RoundedCornerShape(22.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonCyan, NeonViolet))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(Color(0xFF5865F2), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("บัญชี Discord ที่เชื่อมต่อ", color = TextSecondary, fontSize = 10.sp)
                                Text(linkedDiscordTag, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        Surface(
                            color = NeonEmerald.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "🟢 ซิงค์เรียลไทม์",
                                color = NeonEmerald,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("เหรียญ PRIVACY COIN", color = TextSecondary, fontSize = 11.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🪙", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$balanceCoins",
                                    color = NeonAmber,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color.White.copy(alpha = 0.1f)))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("แต้ม GEM POINTS", color = TextSecondary, fontSize = 11.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("💎", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$balanceGems",
                                    color = NeonCyan,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Category Subtabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("💳 เติมเงินอัตโนมัติ", "🎲 กาชาเสี่ยงโชค", "🛍️ ร้านค้าไอเทม").forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        label = {
                            Text(
                                text = title,
                                color = if (isSelected) ObsidianBlack else Color.White,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (index == 0) NeonCyan else if (index == 1) NeonMagenta else NeonViolet,
                            containerColor = ObsidianCard
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(14.dp)) }

        // TAB CONTENT
        when (selectedTab) {
            0 -> {
                // Auto-Payment Gateway Packages
                item {
                    Text(
                        text = "แพ็กเกจเติมเงิน (PromptPay & TrueMoney)",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(packages) { pkg ->
                    Surface(
                        color = ObsidianCard,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable { showPaymentModalForPackage = pkg }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(pkg.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "+${pkg.coins} PC • +${pkg.gemPoints} GEM",
                                    color = NeonCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(pkg.bonusDescription, color = TextSecondary, fontSize = 11.sp)
                            }

                            Button(
                                onClick = { showPaymentModalForPackage = pkg },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("฿${pkg.priceBaht}", color = ObsidianBlack, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            1 -> {
                // Neon Cyber Gacha
                item {
                    Surface(
                        color = Color(0xFF1E0E35),
                        shape = RoundedCornerShape(20.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonMagenta, NeonViolet))),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🎲 CYBER NEON GACHA",
                                color = NeonMagenta,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "ลุ้นรับไอเทมระดับตำนาน SSR & ยศประธานถาวร",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Brush.radialGradient(listOf(NeonMagenta, Color(0xFF280B45)))),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isSpinningGacha) "⚡" else (wonGachaItem?.iconEmoji ?: "🎁"),
                                    fontSize = 46.sp
                                )
                            }

                            wonGachaItem?.let { item ->
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    color = Color(item.rarity.colorHex).copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "${item.rarity.label}: ${item.name}",
                                        color = Color(item.rarity.colorHex),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                    )
                                }
                                Text(item.description, color = TextSecondary, fontSize = 11.sp)
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        if (balanceCoins >= 100) {
                                            isSpinningGacha = true
                                            val item = onPlayGacha(100)
                                            wonGachaItem = item
                                            isSpinningGacha = false
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonMagenta),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("สุ่ม 1 ครั้ง (100 PC)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        if (balanceCoins >= 450) {
                                            isSpinningGacha = true
                                            val item = onPlayGacha(450)
                                            wonGachaItem = item
                                            isSpinningGacha = false
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("สุ่ม 5 ครั้ง (450 PC)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // Item Shop
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(
                            Triple("ตั๋ว Gemini Live API 30 วัน", "250 PC", "ใช้งานคุยสดด้วยเสียงไม่จำกัดพร้อมถอดความ"),
                            Triple("ตั๋วดูหนัง 4K Cinema Room Pass", "150 PC", "เปิดห้องดูหนังพร้อมเพื่อนซิงค์แบบ 4K"),
                            Triple("กรอบโปรไฟล์นีออนโฮโลแกรม VIP", "300 PC", "กรอบเรืองแสงโชว์ในแชทและคอมเมนต์คลิป")
                        ).forEach { (name, price, desc) ->
                            Surface(
                                color = ObsidianCard,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(desc, color = TextSecondary, fontSize = 11.sp)
                                    }
                                    Button(
                                        onClick = { /* buy */ },
                                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(price, color = ObsidianBlack, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(100.dp)) }
    }

    // Payment Gateway Dialog (PromptPay / TrueMoney Simulation)
    showPaymentModalForPackage?.let { pkg ->
        var selectedMethod by remember { mutableStateOf("PromptPay") }

        AlertDialog(
            onDismissRequest = { showPaymentModalForPackage = null },
            containerColor = ObsidianCard,
            title = {
                Text(
                    text = "ชำระเงินอัตโนมัติ: ${pkg.title}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ยอดชำระ: ฿${pkg.priceBaht} บาท",
                        color = NeonCyan,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = selectedMethod == "PromptPay",
                            onClick = { selectedMethod = "PromptPay" },
                            label = { Text("PromptPay QR") }
                        )
                        FilterChip(
                            selected = selectedMethod == "TrueMoney",
                            onClick = { selectedMethod = "TrueMoney" },
                            label = { Text("TrueMoney") }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // QR Code visual box simulation
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.QrCode2, contentDescription = null, tint = Color.Black, modifier = Modifier.size(80.dp))
                            Text("พร้อมเพย์อัตโนมัติ", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text("ระบบเช็คสลิป 1 วิ", color = Color.Gray, fontSize = 8.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "เงินจะเข้ากระเป๋าประธานโดยตรง และปลดล็อกยศ VIP ให้คุณทันที",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val tier = if (pkg.priceBaht >= 399) VipTier.PRESIDENT_VIP else VipTier.VIP
                        onTopUpSuccess(pkg.coins, pkg.gemPoints, tier)
                        showPaymentModalForPackage = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald)
                ) {
                    Text("จำลองสลิปชำระสำเร็จ", color = ObsidianBlack, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPaymentModalForPackage = null }) {
                    Text("ยกเลิก", color = TextSecondary)
                }
            }
        )
    }
}
