package com.example.data

import com.example.model.*

object SampleDataProvider {

    fun getInitialMovies(): List<Movie> = listOf(
        Movie(
            id = "m1",
            title = "ไซเบอร์พังค์ ฮอไรซอน 2099",
            englishTitle = "CYBERPUNK HORIZON 2099",
            duration = "2 ชม. 14 นาที",
            rating = "9.8",
            genres = listOf("ไซไฟ", "แอ็กชัน", "อนาคต", "ระทึกขวัญ"),
            year = "2026",
            description = "ในมหานครนีโอเคียวโตะปี 2099 เอเจนต์ลับต้องกอบกู้โค้ดสุดท้ายที่กำหนดชะตากรรมของมนุษยชาติก่อนที่ระบบไซเบอร์ไดรฟ์จะกลืนกินทุกสิ่ง สตรีมระบบภาพ 4K Ultra HDR และระบบเสียง Dolby Atmos ส่วนตัว",
            posterDrawableName = "movie_hero_cyberpunk",
            isFeatured = true,
            totalSeconds = 8040,
            watchPartyActive = true,
            currentWatchers = 245
        ),
        Movie(
            id = "m2",
            title = "ขอบฟ้าหลุมดำ: สุดขอบมิติ",
            englishTitle = "EVENT HORIZON: THE EDGE OF REALITY",
            duration = "1 ชม. 58 นาที",
            rating = "9.5",
            genres = listOf("ไซไฟ", "อวกาศ", "สืบสวน"),
            year = "2025",
            description = "การเดินทางสู่ใจกลางรูหนอนควอนตัมของสองนักบินอวกาศเพื่อตามหาสัญญาณชีพที่ขาดหายไป ภาพยนตร์อวกาศที่ภาพสวยสะกดตาและเต็มไปด้วยความลี้ลับเกินจินตนาการ",
            posterDrawableName = "movie_event_horizon",
            isFeatured = false,
            totalSeconds = 7080,
            watchPartyActive = true,
            currentWatchers = 189
        ),
        Movie(
            id = "m3",
            title = "โค้ดเงา: ภารกิจกรุงเทพ 2030",
            englishTitle = "SHADOW CODE: BANGKOK PROTOCOL",
            duration = "1 ชม. 45 นาที",
            rating = "9.2",
            genres = listOf("แอ็กชัน", "จารกรรม", "ไซเบอร์"),
            year = "2026",
            description = "ภารกิจไล่ล่าสุดระทึกข้ามแม่น้ำเจ้าพระยาและตึกระฟ้าของกรุงเทพฯ การต่อสู้ด้วยแฮกกิ้งและศิลปะการต่อสู้ระยะประชิด",
            posterDrawableName = "movie_hero_cyberpunk",
            isFeatured = false,
            totalSeconds = 6300,
            watchPartyActive = false,
            currentWatchers = 94
        ),
        Movie(
            id = "m4",
            title = "โซล เมทริกซ์ ออนไลน์",
            englishTitle = "SOUL MATRIX ONLINE",
            duration = "24 ตอน (ซีรีส์)",
            rating = "9.6",
            genres = listOf("แอนิเมชัน", "เกม", "แฟนตาซี"),
            year = "2026",
            description = "เมื่อจิตวิญญาณของผู้เล่น 100 คนถูกดึงเข้าสู่เกมเมทริกซ์โบราณ พวกเขาต้องร่วมมือกันโค่นบอสสุดท้ายเพื่อกลับสู่โลกแห่งความจริง",
            posterDrawableName = "movie_event_horizon",
            isFeatured = false,
            totalSeconds = 1440,
            watchPartyActive = true,
            currentWatchers = 412
        )
    )

    fun getInitialStories(): List<StoryItem> = listOf(
        StoryItem(
            id = "s_me",
            userId = "user_me",
            userName = "สตอรี่ของคุณ",
            caption = "แตะเพื่อลงสตอรี่ใหม่ ✨",
            timeAgo = "ตอนนี้",
            musicTrack = null,
            backgroundGradientColors = listOf(0xFF00F2FE, 0xFF4FACFE),
            stickerEmoji = "📸",
            isMyStory = true,
            isViewed = false
        ),
        StoryItem(
            id = "s1",
            userId = "u1",
            userName = "แพรว Ploy",
            caption = "ฉากต่อสู้ใน Cyberpunk Horizon คือที่สุด! ดูรอบที่ 2 แล้วยังขนลุก 🍿🔥",
            timeAgo = "15 นาทีที่แล้ว",
            musicTrack = "Synthwave Pulse 2099",
            backgroundGradientColors = listOf(0xFF8A2BE2, 0xFFFF007F),
            stickerEmoji = "🍿",
            isMyStory = false,
            isViewed = false
        ),
        StoryItem(
            id = "s2",
            userId = "u2",
            userName = "นนท์ Cyber",
            caption = "กำลังทดสอบแชร์หน้าจอเล่นเกมแบบความหน่วง 0ms ลื่นมากก 🎮⚡",
            timeAgo = "1 ชม. ที่แล้ว",
            musicTrack = "Cyber Odyssey - Neon Drift",
            backgroundGradientColors = listOf(0xFF00F2FE, 0xFF0072FF),
            stickerEmoji = "🎮",
            isMyStory = false,
            isViewed = false
        ),
        StoryItem(
            id = "s3",
            userId = "u3",
            userName = "มายด์ CineClub",
            caption = "ใครยังไม่นอน คืนนี้มี Watch Party ดู Event Horizon ตี 1 กดเข้าลิ้งก์ในแชทเลยนะ! 🚀🌌",
            timeAgo = "2 ชม. ที่แล้ว",
            musicTrack = "Space Ambient Meditation",
            backgroundGradientColors = listOf(0xFF1F1C2C, 0xFF928DAB),
            stickerEmoji = "🌌",
            isMyStory = false,
            isViewed = false
        ),
        StoryItem(
            id = "s4",
            userId = "u4",
            userName = "บอส BossSound",
            caption = "ระบบส่งเสียง Voice Note ใน PRIVACY-LIVE คมชัดมาก บันทึกไว 🎙️✨",
            timeAgo = "4 ชม. ที่แล้ว",
            musicTrack = "Lo-Fi Midnight Bangkok",
            backgroundGradientColors = listOf(0xFFF12711, 0xFFF5AF19),
            stickerEmoji = "🎙️",
            isMyStory = false,
            isViewed = true
        )
    )

    fun getInitialClips(): List<ClipItem> = listOf(
        ClipItem(
            id = "c1",
            creatorName = "Ploy Movie Review",
            creatorHandle = "@ploy_cinephile",
            title = "ฉากขับรถไล่ล่าที่สมจริงที่สุดในภาพยนตร์ปี 2026! ใช้กราฟิกเรนเดอร์แบบไหนมาดูกัน 🏎️💥 #cyberpunk #หนังไซไฟ",
            tags = listOf("#cyberpunk", "#หนังใหม่2026", "#รีวิวหนัง", "#privacylive"),
            soundTrack = "Original Soundtrack - Neon Horizon Drive",
            likesCount = 14200,
            commentsCount = 385,
            sharesCount = 920,
            isLiked = false,
            privacyLabel = "สาธารณะ",
            gradientColors = listOf(0xFF0B192C, 0xFF1E3E62, 0xFF000000)
        ),
        ClipItem(
            id = "c2",
            creatorName = "Tech Secret Thailand",
            creatorHandle = "@tech_privacy",
            title = "วิธีดูหนังพร้อมคุยสายและแชร์หน้าจอไปด้วยกันแบบเข้ารหัสส่วนตัว 100% ไม่มีใครดักข้อมูลได้ 🔒📱",
            tags = listOf("#เคล็ดลับ", "#แชร์หน้าจอ", "#privacylive", "#แชทลับ"),
            soundTrack = "Future Tech Beat - Cyber Bangkok",
            likesCount = 8900,
            commentsCount = 210,
            sharesCount = 640,
            isLiked = true,
            privacyLabel = "สาธารณะ",
            gradientColors = listOf(0xFF141E30, 0xFF243B55, 0xFF000000)
        ),
        ClipItem(
            id = "c3",
            creatorName = "Galaxy Explorer",
            creatorHandle = "@space_nerd",
            title = "ถ้ามนุษย์ตกเข้าไปในหลุมดำ Event Horizon จะเกิดอะไรขึ้น? ทฤษฎีฟิสิกส์ที่น่าขนลุกที่สุด 🌌🕳️",
            tags = listOf("#อวกาศ", "#eventhorizon", "#หลุมดำ", "#วิทยาศาสตร์"),
            soundTrack = "Interstellar Organ Remix",
            likesCount = 23400,
            commentsCount = 740,
            sharesCount = 1890,
            isLiked = false,
            privacyLabel = "สาธารณะ",
            gradientColors = listOf(0xFF0F0C29, 0xFF302B63, 0xFF24243E)
        )
    )

    fun getInitialChats(): List<ChatConversation> = listOf(
        ChatConversation(
            id = "chat_gemini_live",
            contactName = "🎙️ Gemini Live AI (ผู้ช่วยภาพยนตร์ & VIP)",
            avatarColor = 0xFF00F2FE,
            isOnline = true,
            statusText = "🟢 พร้อมคุยสดด้วยเสียงแบบ Streaming เรียลไทม์",
            lastMessage = "✨ สวัสดีครับท่านประธาน! ผมพร้อมวิเคราะห์หนังและช่วยงานระบบแล้ว",
            lastMessageTime = "14:35",
            unreadCount = 1,
            isEncrypted = true,
            messages = listOf(
                ChatMessage(
                    id = "g_1",
                    senderName = "Gemini Live AI",
                    isSelf = false,
                    content = "ยินดีต้อนรับสู่ PRIVACY-LIVE ครับ! คุณสามารถกดปุ่มโทรหรือส่งข้อความเสียงเพื่อสนทนาสดแบบ Streaming กับผมได้ตลอด 24 ชั่วโมง",
                    time = "14:30",
                    messageType = MessageType.TEXT
                ),
                ChatMessage(
                    id = "g_2",
                    senderName = "Gemini Live AI",
                    isSelf = false,
                    content = "แนะนำหนังประจำสัปดาห์: ไซเบอร์พังค์ ฮอไรซอน 2099 รองรับภาพ 4K HDR และระบบเสียง Dolby Atmos แตะดูพร้อมเพื่อนได้เลยครับ!",
                    time = "14:32",
                    messageType = MessageType.MOVIE_INVITE,
                    movieTitle = "ไซเบอร์พังค์ ฮอไรซอน 2099"
                )
            )
        ),
        ChatConversation(
            id = "chat_1",
            contactName = "แพรว Ploy (Cinephile)",
            avatarColor = 0xFFFF007F,
            isOnline = true,
            statusText = "กำลังดู Cyberpunk Horizon 2099",
            lastMessage = "🎙️ ข้อความเสียง (0:12)",
            lastMessageTime = "14:28",
            unreadCount = 1,
            isEncrypted = true,
            messages = listOf(
                ChatMessage(
                    id = "m_1",
                    senderName = "แพรว Ploy (Cinephile)",
                    isSelf = false,
                    content = "เธอออ! ว่างดูหนังด้วยกันไหม คืนนี้มี Watch Party หนังใหม่ล้ำมาก",
                    time = "14:20",
                    messageType = MessageType.TEXT
                ),
                ChatMessage(
                    id = "m_2",
                    senderName = "คุณ",
                    isSelf = true,
                    content = "ว่างเลย เรื่องไหนเหรอ Cyberpunk หรือ Event Horizon?",
                    time = "14:22",
                    messageType = MessageType.TEXT
                ),
                ChatMessage(
                    id = "m_3",
                    senderName = "แพรว Ploy (Cinephile)",
                    isSelf = false,
                    content = "ส่งคำเชิญชมพร้อมกัน: ไซเบอร์พังค์ ฮอไรซอน 2099",
                    time = "14:25",
                    messageType = MessageType.MOVIE_INVITE,
                    movieTitle = "ไซเบอร์พังค์ ฮอไรซอน 2099"
                ),
                ChatMessage(
                    id = "m_4",
                    senderName = "แพรว Ploy (Cinephile)",
                    isSelf = false,
                    content = "ฟังอันนี้นะ ตื่นเต้นมาก!",
                    time = "14:28",
                    messageType = MessageType.VOICE_NOTE,
                    voiceDurationSeconds = 12,
                    voiceWaveformAmplitudes = listOf(0.3f, 0.6f, 0.9f, 0.4f, 0.8f, 1.0f, 0.7f, 0.5f, 0.85f, 0.6f, 0.3f, 0.7f, 0.9f, 0.5f, 0.2f),
                    isPlayingVoice = false,
                    voiceCurrentProgress = 0f
                )
            )
        ),
        ChatConversation(
            id = "chat_2",
            contactName = "นนท์ Cyber (Gamer)",
            avatarColor = 0xFF00F2FE,
            isOnline = true,
            statusText = "พร้อมแชร์หน้าจอ / สตรีมเกม",
            lastMessage = "ลองกดปุ่มโทรแล้วแชร์หน้าจอให้ดูหน่อย",
            lastMessageTime = "13:45",
            unreadCount = 0,
            isEncrypted = true,
            messages = listOf(
                ChatMessage(
                    id = "n_1",
                    senderName = "นนท์ Cyber (Gamer)",
                    isSelf = false,
                    content = "เฮ้ย ในแอปนี้โทรคุยแล้วกดแชร์หน้าจอได้ลื่นจัดเลย",
                    time = "13:40",
                    messageType = MessageType.TEXT
                ),
                ChatMessage(
                    id = "n_2",
                    senderName = "คุณ",
                    isSelf = true,
                    content = "จริงป่ะ เดี๋ยวโทรไปลองกดแชร์หน้าจอนะ",
                    time = "13:43",
                    messageType = MessageType.TEXT
                ),
                ChatMessage(
                    id = "n_3",
                    senderName = "นนท์ Cyber (Gamer)",
                    isSelf = false,
                    content = "จัดมาเลย รอรับสายอยู่!",
                    time = "13:45",
                    messageType = MessageType.TEXT
                )
            )
        ),
        ChatConversation(
            id = "chat_3",
            contactName = "มายด์ CineClub (VIP)",
            avatarColor = 0xFF8A2BE2,
            isOnline = false,
            statusText = "ออนไลน์เมื่อ 10 นาทีที่แล้ว",
            lastMessage = "ไว้คืนนี้เจอกันในห้อง Watch Party นะคะ",
            lastMessageTime = "12:15",
            unreadCount = 0,
            isEncrypted = true,
            messages = emptyList()
        ),
        ChatConversation(
            id = "chat_4",
            contactName = "บอส BossSound",
            avatarColor = 0xFF00F5A0,
            isOnline = true,
            statusText = "เสียงใส ไมค์สเตอริโอ",
            lastMessage = "ส่งไฟล์เสียงบันทึกเสียงให้แล้วนะครับ",
            lastMessageTime = "11:02",
            unreadCount = 0,
            isEncrypted = true,
            messages = emptyList()
        )
    )
}
