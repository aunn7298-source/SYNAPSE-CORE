package com.example.model

enum class CallType {
    VOICE,
    VIDEO
}

enum class CallConnectionState(
    val labelThai: String,
    val statusColorHex: Long,
    val subText: String
) {
    CONNECTING("กำลังเชื่อมต่อ...", 0xFF00F2FE, "กำลังสร้างช่องสัญญาณ E2EE ปลอดภัย"),
    CONNECTED("เชื่อมต่อสำเร็จ", 0xFF00F5A0, "สัญญาณเสียงระดับ HD สเตอริโอ"),
    LISTENING("กำลังฟังเสียง...", 0xFF00F5A0, "ระบบตัดเสียงรบกวนอัจฉริยะทำงาน"),
    SPEAKING("กำลังพูด / ถ่ายทอดเสียง...", 0xFFFF007F, "กำลังส่งข้อมูลเสียงแบบเรียลไทม์"),
    SCREEN_SHARING("กำลังแชร์หน้าจอสด", 0xFF00F2FE, "1080p 60fps • ค่าความหน่วง 14ms"),
    DISCONNECTED("ตัดสายแล้ว", 0xFFFF3366, "สิ้นสุดการสนทนาอย่างปลอดภัย")
}

data class ActiveCall(
    val contactName: String,
    val callType: CallType,
    val connectionState: CallConnectionState = CallConnectionState.CONNECTING,
    val durationSeconds: Int = 0,
    val isConnected: Boolean = true,
    val isMuted: Boolean = false,
    val isSpeakerOn: Boolean = true,
    val isVideoEnabled: Boolean = true,
    val isFrontCamera: Boolean = true,
    val isScreenSharing: Boolean = false,
    val isMinimized: Boolean = false,
    val audioWaveIntensity: Float = 0.6f,
    val screenShareTargetName: String = "หน้าจอสมาร์ทโฟน (PRIVACY-LIVE Display)"
)
