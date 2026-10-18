package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleDataProvider
import com.example.data.local.AppDatabase
import com.example.data.local.entity.CallRecordEntity
import com.example.data.local.entity.ChatMessageEntity
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class NavigationTab {
    CINEMA,
    CLIPS,
    STORIES,
    CHAT,
    PROFILE
}

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val chatDao = db.chatDao()
    private val callDao = db.callRecordDao()

    // Biometric & PIN Security Vault Lock
    private val _isVaultLocked = MutableStateFlow(false)
    val isVaultLocked: StateFlow<Boolean> = _isVaultLocked.asStateFlow()

    fun unlockVault() { _isVaultLocked.value = false }
    fun lockVault() { _isVaultLocked.value = true }

    // Navigation Tab
    private val _currentTab = MutableStateFlow(NavigationTab.CINEMA)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    fun selectTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    // Movies & Cinema
    private val _movies = MutableStateFlow(SampleDataProvider.getInitialMovies())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _activeMovie = MutableStateFlow<Movie?>(null)
    val activeMovie: StateFlow<Movie?> = _activeMovie.asStateFlow()

    // Watch Room & Co-watching
    private val _activeWatchRoom = MutableStateFlow<WatchRoom?>(null)
    val activeWatchRoom: StateFlow<WatchRoom?> = _activeWatchRoom.asStateFlow()

    fun createWatchRoom(movie: Movie, roomName: String = "ห้องดูหนังส่วนตัว") {
        val code = "PV-" + (1000..9999).random()
        val initialRoom = WatchRoom(
            roomId = UUID.randomUUID().toString(),
            roomCode = code,
            roomName = roomName,
            movie = movie,
            hostName = "คุณ",
            isPrivate = true,
            participants = listOf("คุณ (Host)", "แพรว Ploy", "นนท์ Cyber"),
            chatMessages = listOf(
                RoomChatMessage(
                    id = "sys_1",
                    senderName = "ระบบซิงค์",
                    message = "ห้องดูหนังส่วนตัวสร้างเรียบร้อย! รหัสห้อง: $code (กำลังซิงค์เวลาฉาย)",
                    time = "14:40",
                    isSystemSyncNotice = true
                ),
                RoomChatMessage(
                    id = "c_1",
                    senderName = "แพรว Ploy",
                    message = "พร้อมดูแล้ววว ภาพ 4K ชัดมากกก 🍿",
                    time = "14:40"
                )
            ),
            isPlaybackSynced = true
        )
        _activeWatchRoom.value = initialRoom
        playMovie(movie, asWatchParty = true)
    }

    fun sendRoomChatMessage(text: String) {
        if (text.isBlank()) return
        val newMsg = RoomChatMessage(
            id = UUID.randomUUID().toString(),
            senderName = "คุณ",
            message = text.trim(),
            time = "14:42"
        )
        _activeWatchRoom.update { room ->
            room?.copy(chatMessages = room.chatMessages + newMsg)
        }
    }

    fun leaveWatchRoom() {
        _activeWatchRoom.value = null
        closeMoviePlayer()
    }

    private val _isMoviePlaying = MutableStateFlow(false)
    val isMoviePlaying: StateFlow<Boolean> = _isMoviePlaying.asStateFlow()

    private val _moviePlaybackSeconds = MutableStateFlow(124)
    val moviePlaybackSeconds: StateFlow<Int> = _moviePlaybackSeconds.asStateFlow()

    private val _isWatchParty = MutableStateFlow(false)
    val isWatchParty: StateFlow<Boolean> = _isWatchParty.asStateFlow()

    private val _watchReactions = MutableStateFlow<List<WatchReaction>>(emptyList())
    val watchReactions: StateFlow<List<WatchReaction>> = _watchReactions.asStateFlow()

    private val _selectedQuality = MutableStateFlow("4K Ultra HDR")
    val selectedQuality: StateFlow<String> = _selectedQuality.asStateFlow()

    private val _selectedSubtitle = MutableStateFlow("ภาษาไทย (TH)")
    val selectedSubtitle: StateFlow<String> = _selectedSubtitle.asStateFlow()

    private val _selectedAudio = MutableStateFlow("เสียงไทย 5.1 Dolby Atmos")
    val selectedAudio: StateFlow<String> = _selectedAudio.asStateFlow()

    private var movieTickerJob: Job? = null

    fun playMovie(movie: Movie, asWatchParty: Boolean = false) {
        _activeMovie.value = movie
        _isWatchParty.value = asWatchParty
        _isMoviePlaying.value = true
        _moviePlaybackSeconds.value = 124
        startMovieTicker()
    }

    fun toggleMoviePlayPause() {
        _isMoviePlaying.value = !_isMoviePlaying.value
        if (_isMoviePlaying.value) {
            startMovieTicker()
        } else {
            movieTickerJob?.cancel()
        }
    }

    fun seekMovieTo(seconds: Int) {
        _moviePlaybackSeconds.value = seconds.coerceIn(0, _activeMovie.value?.totalSeconds ?: 7200)
    }

    fun rewindMovie(seconds: Int = 10) { seekMovieTo(_moviePlaybackSeconds.value - seconds) }
    fun forwardMovie(seconds: Int = 10) { seekMovieTo(_moviePlaybackSeconds.value + seconds) }

    fun closeMoviePlayer() {
        movieTickerJob?.cancel()
        _activeMovie.value = null
        _isMoviePlaying.value = false
    }

    fun setQuality(quality: String) { _selectedQuality.value = quality }
    fun setSubtitle(sub: String) { _selectedSubtitle.value = sub }
    fun setAudio(audio: String) { _selectedAudio.value = audio }

    fun sendWatchReaction(emoji: String) {
        val newReaction = WatchReaction(
            id = UUID.randomUUID().toString(),
            emoji = emoji,
            sender = "คุณ",
            xOffset = (10..90).random() / 100f
        )
        _watchReactions.update { (it + newReaction).takeLast(12) }
    }

    private fun startMovieTicker() {
        movieTickerJob?.cancel()
        movieTickerJob = viewModelScope.launch {
            while (_isMoviePlaying.value) {
                delay(1000)
                _moviePlaybackSeconds.update { current ->
                    val maxSec = _activeMovie.value?.totalSeconds ?: 7200
                    if (current < maxSec) current + 1 else 0
                }
            }
        }
    }

    // Clips / Reels
    private val _clips = MutableStateFlow(SampleDataProvider.getInitialClips())
    val clips: StateFlow<List<ClipItem>> = _clips.asStateFlow()

    private val _clipComments = MutableStateFlow<Map<String, List<ClipComment>>>(
        mapOf(
            "c1" to listOf(
                ClipComment("1", "มอส Cine", "มุมกล้องโคตรสวย นีออนสะท้อนพื้นถนนสุดยอด!", "5 นาทีที่แล้ว", 18),
                ClipComment("2", "แป้ง สตูดิโอ", "อยากไปดูในโรงแล้ววว พากย์ไทยเข้าวันไหนคะ", "12 นาทีที่แล้ว", 7)
            ),
            "c2" to listOf(
                ClipComment("3", "CyberNinja", "แชร์หน้าจอชัดจริง ความหน่วงแทบไม่มีเลย เยี่ยมมาก", "2 นาทีที่แล้ว", 34)
            )
        )
    )
    val clipComments: StateFlow<Map<String, List<ClipComment>>> = _clipComments.asStateFlow()

    private val _activeCommentsClipId = MutableStateFlow<String?>(null)
    val activeCommentsClipId: StateFlow<String?> = _activeCommentsClipId.asStateFlow()

    fun openComments(clipId: String) { _activeCommentsClipId.value = clipId }
    fun closeComments() { _activeCommentsClipId.value = null }

    fun toggleLikeClip(clipId: String) {
        _clips.update { list ->
            list.map { clip ->
                if (clip.id == clipId) {
                    val newLiked = !clip.isLiked
                    clip.copy(
                        isLiked = newLiked,
                        likesCount = if (newLiked) clip.likesCount + 1 else clip.likesCount - 1
                    )
                } else clip
            }
        }
    }

    fun addClipComment(clipId: String, text: String) {
        if (text.isBlank()) return
        val newComment = ClipComment(
            id = UUID.randomUUID().toString(),
            author = "คุณ (Privacy User)",
            text = text.trim(),
            timeAgo = "เมื่อสักครู่",
            likes = 0
        )
        _clipComments.update { map ->
            val existing = map[clipId] ?: emptyList()
            map + (clipId to (listOf(newComment) + existing))
        }
        _clips.update { list ->
            list.map { if (it.id == clipId) it.copy(commentsCount = it.commentsCount + 1) else it }
        }
    }

    fun postNewClip(title: String, tags: List<String>, privacy: String) {
        val newClip = ClipItem(
            id = "c_${System.currentTimeMillis()}",
            creatorName = "คุณ (Creator)",
            creatorHandle = "@my_privacy_clip",
            title = title,
            tags = tags,
            soundTrack = "PRIVACY-LIVE Trending Audio #1",
            likesCount = 1,
            commentsCount = 0,
            sharesCount = 0,
            isLiked = true,
            privacyLabel = privacy,
            gradientColors = listOf(0xFF2E0854, 0xFF4A0E4E, 0xFF000000)
        )
        _clips.update { listOf(newClip) + it }
    }

    // Stories
    private val _stories = MutableStateFlow(SampleDataProvider.getInitialStories())
    val stories: StateFlow<List<StoryItem>> = _stories.asStateFlow()

    private val _activeStory = MutableStateFlow<StoryItem?>(null)
    val activeStory: StateFlow<StoryItem?> = _activeStory.asStateFlow()

    fun openStory(story: StoryItem) {
        _activeStory.value = story
        _stories.update { list ->
            list.map { if (it.id == story.id) it.copy(isViewed = true) else it }
        }
    }

    fun closeStory() { _activeStory.value = null }

    fun postNewStory(caption: String, emoji: String, music: String?) {
        val newStory = StoryItem(
            id = "s_${System.currentTimeMillis()}",
            userId = "user_me",
            userName = "สตอรี่ของคุณ",
            caption = caption,
            timeAgo = "เมื่อสักครู่",
            musicTrack = music ?: "Neon Vibes 2026",
            backgroundGradientColors = listOf(0xFF00F2FE, 0xFF8A2BE2),
            stickerEmoji = emoji,
            isMyStory = true,
            isViewed = false
        )
        _stories.update { list ->
            val filtered = list.filter { !it.isMyStory }
            listOf(newStory) + filtered
        }
    }

    // Private Chat & Voice Messaging with Room DB
    private val _conversations = MutableStateFlow(SampleDataProvider.getInitialChats())
    val conversations: StateFlow<List<ChatConversation>> = _conversations.asStateFlow()

    private val _activeChatId = MutableStateFlow<String?>(null)
    val activeChatId: StateFlow<String?> = _activeChatId.asStateFlow()

    fun openChat(chatId: String) {
        _activeChatId.value = chatId
        _conversations.update { list ->
            list.map { if (it.id == chatId) it.copy(unreadCount = 0) else it }
        }
    }

    fun closeChat() { _activeChatId.value = null }

    fun sendTextMessage(chatId: String, text: String) {
        if (text.isBlank()) return
        val newMsgId = UUID.randomUUID().toString()
        val newMsg = ChatMessage(
            id = newMsgId,
            senderName = "คุณ",
            isSelf = true,
            content = text.trim(),
            time = "14:32",
            messageType = MessageType.TEXT
        )
        appendChatMessage(chatId, newMsg, text.trim())

        // Save to Room DB asynchronously
        viewModelScope.launch {
            chatDao.insertMessage(
                ChatMessageEntity(
                    id = newMsgId,
                    conversationId = chatId,
                    senderName = "คุณ",
                    isSelf = true,
                    content = text.trim(),
                    time = "14:32",
                    messageType = "TEXT"
                )
            )

            // Gemini Live AI interactive response
            if (chatId == "chat_gemini_live") {
                delay(800)
                val aiReply = when {
                    text.contains("หนัง", ignoreCase = true) || text.contains("movie", ignoreCase = true) ->
                        "🎬 คำแนะนำภาพยนตร์: ไซเบอร์พังค์ ฮอไรซอน 2099 และ ขอบฟ้าหลุมดำ Event Horizon กำลังเป็นที่นิยมมากในหมู่วีไอพี คุณสามารถกดเปิด Watch Party ชมพร้อมกันได้เลยครับ!"
                    text.contains("เงิน", ignoreCase = true) || text.contains("เหรียญ", ignoreCase = true) || text.contains("vip", ignoreCase = true) ->
                        "💳 ระบบ Social-Fi ซิงค์ตรงกับบัญชี Discord ($linkedDiscordTag) เติมเงินผ่าน PromptPay ได้รับเหรียญและสิทธิ์ VIP อัตโนมัติใน 1 วินาทีครับ"
                    text.contains("โทร", ignoreCase = true) || text.contains("เสียง", ignoreCase = true) || text.contains("live", ignoreCase = true) ->
                        "🎙️ คุณสามารถกดปุ่มโทรด้านบนเพื่อคุยสดด้วยเสียง (Gemini Live Audio Streaming) กับผมได้เลยครับ ระบบจะแปลงเสียงเป็นข้อความและตอบกลับทันที!"
                    else ->
                        "✨ Gemini Live AI ได้ยินคุณแล้ว: ระบบความปลอดภัยระดับ E2EE และ Guardian Auto-Restart 24 ชม. ทำงานปกติ ไม่มีข้อผิดพลาดครับ"
                }
                val aiMsgId = UUID.randomUUID().toString()
                val aiMsg = ChatMessage(
                    id = aiMsgId,
                    senderName = "Gemini Live AI",
                    isSelf = false,
                    content = aiReply,
                    time = "14:33",
                    messageType = MessageType.TEXT
                )
                appendChatMessage(chatId, aiMsg, aiReply)
                chatDao.insertMessage(
                    ChatMessageEntity(
                        id = aiMsgId,
                        conversationId = chatId,
                        senderName = "Gemini Live AI",
                        isSelf = false,
                        content = aiReply,
                        time = "14:33",
                        messageType = "TEXT"
                    )
                )
            }
        }
    }

    // Voice Recording
    private val _isRecordingVoice = MutableStateFlow(false)
    val isRecordingVoice: StateFlow<Boolean> = _isRecordingVoice.asStateFlow()

    private val _recordingDurationSec = MutableStateFlow(0)
    val recordingDurationSec: StateFlow<Int> = _recordingDurationSec.asStateFlow()

    private var voiceRecordingJob: Job? = null

    fun startVoiceRecording() {
        _isRecordingVoice.value = true
        _recordingDurationSec.value = 0
        voiceRecordingJob?.cancel()
        voiceRecordingJob = viewModelScope.launch {
            while (_isRecordingVoice.value) {
                delay(1000)
                _recordingDurationSec.update { it + 1 }
            }
        }
    }

    fun stopAndSendVoiceRecording(chatId: String) {
        val duration = _recordingDurationSec.value.coerceAtLeast(1)
        _isRecordingVoice.value = false
        voiceRecordingJob?.cancel()

        val amplitudes = List(16) { ((it * 37) % 8 + 3) / 10f }
        val newMsgId = UUID.randomUUID().toString()
        val newVoiceMsg = ChatMessage(
            id = newMsgId,
            senderName = "คุณ",
            isSelf = true,
            content = "ข้อความเสียง",
            time = "14:33",
            messageType = MessageType.VOICE_NOTE,
            voiceDurationSeconds = duration,
            voiceWaveformAmplitudes = amplitudes,
            isPlayingVoice = false,
            voiceCurrentProgress = 0f
        )
        appendChatMessage(chatId, newVoiceMsg, "🎙️ ข้อความเสียง ($duration วิ)")

        // Save Voice Note to Room DB
        viewModelScope.launch {
            chatDao.insertMessage(
                ChatMessageEntity(
                    id = newMsgId,
                    conversationId = chatId,
                    senderName = "คุณ",
                    isSelf = true,
                    content = "🎙️ ข้อความเสียง ($duration วิ)",
                    time = "14:33",
                    messageType = "VOICE_NOTE",
                    voiceDurationSeconds = duration
                )
            )
        }
    }

    fun cancelVoiceRecording() {
        _isRecordingVoice.value = false
        voiceRecordingJob?.cancel()
        _recordingDurationSec.value = 0
    }

    private var voicePlayJob: Job? = null

    fun togglePlayVoiceMessage(chatId: String, messageId: String) {
        _conversations.update { list ->
            list.map { conv ->
                if (conv.id == chatId) {
                    conv.copy(messages = conv.messages.map { msg ->
                        if (msg.id == messageId) {
                            val newPlayState = !msg.isPlayingVoice
                            msg.copy(isPlayingVoice = newPlayState)
                        } else {
                            msg.copy(isPlayingVoice = false)
                        }
                    })
                } else conv
            }
        }

        voicePlayJob?.cancel()
        voicePlayJob = viewModelScope.launch {
            for (step in 1..20) {
                delay(200)
                _conversations.update { list ->
                    list.map { conv ->
                        if (conv.id == chatId) {
                            conv.copy(messages = conv.messages.map { msg ->
                                if (msg.id == messageId && msg.isPlayingVoice) {
                                    val progress = step / 20f
                                    if (step == 20) {
                                        msg.copy(isPlayingVoice = false, voiceCurrentProgress = 0f)
                                    } else {
                                        msg.copy(voiceCurrentProgress = progress)
                                    }
                                } else msg
                            })
                        } else conv
                    }
                }
            }
        }
    }

    private fun appendChatMessage(chatId: String, message: ChatMessage, lastText: String) {
        _conversations.update { list ->
            list.map { conv ->
                if (conv.id == chatId) {
                    conv.copy(
                        messages = conv.messages + message,
                        lastMessage = lastText,
                        lastMessageTime = "14:33"
                    )
                } else conv
            }
        }
    }

    // Calling & Screen Sharing State (CallState Engine)
    private val _activeCall = MutableStateFlow<ActiveCall?>(null)
    val activeCall: StateFlow<ActiveCall?> = _activeCall.asStateFlow()

    private var callDurationJob: Job? = null
    private var callStateSimulationJob: Job? = null

    fun startCall(contactName: String, callType: CallType) {
        _activeCall.value = ActiveCall(
            contactName = contactName,
            callType = callType,
            connectionState = CallConnectionState.CONNECTING,
            durationSeconds = 0,
            isConnected = true,
            isMuted = false,
            isSpeakerOn = true,
            isVideoEnabled = (callType == CallType.VIDEO),
            isScreenSharing = false
        )

        // Real-time state simulation: CONNECTING -> CONNECTED -> LISTENING / SPEAKING
        callStateSimulationJob?.cancel()
        callStateSimulationJob = viewModelScope.launch {
            delay(1500)
            _activeCall.update { it?.copy(connectionState = CallConnectionState.CONNECTED) }
            delay(1000)
            while (_activeCall.value != null) {
                _activeCall.update {
                    if (it == null || it.isScreenSharing) it else {
                        it.copy(
                            connectionState = CallConnectionState.LISTENING,
                            audioWaveIntensity = 0.4f
                        )
                    }
                }
                delay(3000)
                _activeCall.update {
                    if (it == null || it.isScreenSharing) it else {
                        it.copy(
                            connectionState = CallConnectionState.SPEAKING,
                            audioWaveIntensity = 0.85f
                        )
                    }
                }
                delay(3000)
            }
        }

        callDurationJob?.cancel()
        callDurationJob = viewModelScope.launch {
            while (_activeCall.value != null) {
                delay(1000)
                _activeCall.update { it?.copy(durationSeconds = it.durationSeconds + 1) }
            }
        }
    }

    fun endCall() {
        val currentCall = _activeCall.value
        callDurationJob?.cancel()
        callStateSimulationJob?.cancel()

        if (currentCall != null) {
            _activeCall.value = currentCall.copy(connectionState = CallConnectionState.DISCONNECTED)
            // Save Call Record in Room Database
            viewModelScope.launch {
                callDao.insertCallRecord(
                    CallRecordEntity(
                        id = UUID.randomUUID().toString(),
                        contactName = currentCall.contactName,
                        callType = currentCall.callType.name,
                        durationSeconds = currentCall.durationSeconds,
                        wasScreenShared = currentCall.isScreenSharing,
                        timeFormatted = String.format("%02d:%02d", currentCall.durationSeconds / 60, currentCall.durationSeconds % 60)
                    )
                )
            }
        }

        viewModelScope.launch {
            delay(600)
            _activeCall.value = null
        }
    }

    fun toggleCallMute() {
        _activeCall.update { it?.copy(isMuted = !it.isMuted) }
    }

    fun toggleCallSpeaker() {
        _activeCall.update { it?.copy(isSpeakerOn = !it.isSpeakerOn) }
    }

    fun toggleCallVideo() {
        _activeCall.update { it?.copy(isVideoEnabled = !it.isVideoEnabled) }
    }

    fun toggleCameraSwitch() {
        _activeCall.update { it?.copy(isFrontCamera = !it.isFrontCamera) }
    }

    fun toggleMinimizeCall() {
        _activeCall.update { it?.copy(isMinimized = !it.isMinimized) }
    }

    fun toggleScreenShare() {
        _activeCall.update { call ->
            call?.let {
                val newSharing = !it.isScreenSharing
                it.copy(
                    isScreenSharing = newSharing,
                    connectionState = if (newSharing) CallConnectionState.SCREEN_SHARING else CallConnectionState.CONNECTED
                )
            }
        }
    }

    // Social-Fi & Monetization State
    private val _balanceCoins = MutableStateFlow(1200)
    val balanceCoins: StateFlow<Int> = _balanceCoins.asStateFlow()

    private val _balanceGems = MutableStateFlow(350)
    val balanceGems: StateFlow<Int> = _balanceGems.asStateFlow()

    private val _vipTier = MutableStateFlow(VipTier.VIP)
    val vipTier: StateFlow<VipTier> = _vipTier.asStateFlow()

    val linkedDiscordTag = "Aunn#7298"

    private val _isSocialFiOpen = MutableStateFlow(false)
    val isSocialFiOpen: StateFlow<Boolean> = _isSocialFiOpen.asStateFlow()

    fun openSocialFi() { _isSocialFiOpen.value = true }
    fun closeSocialFi() { _isSocialFiOpen.value = false }

    fun topUp(coins: Int, gems: Int, newTier: VipTier) {
        _balanceCoins.update { it + coins }
        _balanceGems.update { it + gems }
        _vipTier.value = newTier
        _adminStats.update { it.copy(totalRevenueBaht = it.totalRevenueBaht + (coins / 10)) }
        sendDiscordAlert(
            title = "💰 ยอดเงินเข้ากระเป๋าประธานสำเร็จ",
            message = "ผู้ใช้ $linkedDiscordTag เติมเงิน +$coins PC (ยศ: ${newTier.labelThai})",
            severity = "INFO"
        )
    }

    fun playGacha(cost: Int): GachaItem? {
        if (_balanceCoins.value < cost) return null
        _balanceCoins.update { it - cost }

        val gachaPool = listOf(
            GachaItem("g1", "ตั๋ว Gemini Live ตลอดชีพ", GachaRarity.SSR, "👑", "สนทนาเสียง AI ไม่จำกัดตลอดชีพ"),
            GachaItem("g2", "กรอบโปรไฟล์โฮโลแกรมนีออน", GachaRarity.SR, "✨", "กรอบเรืองแสงโชว์ในห้องดูหนัง"),
            GachaItem("g3", "สติ๊กเกอร์แอนิเมชั่น Watch Party", GachaRarity.R, "🍿", "สติ๊กเกอร์ป๊อปคอร์นเรืองแสง"),
            GachaItem("g4", "แพ็กเกจเสียงสเตอริโอ Dolby 7.1", GachaRarity.SR, "🎧", "ระบบเสียงรอบทิศทางแบบไฮเอนด์"),
            GachaItem("g5", "PRIVACY COIN โบนัส 250 PC", GachaRarity.R, "🪙", "เหรียญคืนทุนทันที")
        )
        val won = gachaPool.random()
        if (won.rarity == GachaRarity.SSR) {
            _vipTier.value = VipTier.PRESIDENT_VIP
            sendDiscordAlert(
                title = "🎉 ตู้กาชาแตก SSR!",
                message = "ผู้ใช้ $linkedDiscordTag สุ่มได้ไอเทมระดับตำนาน: ${won.name}",
                severity = "INFO"
            )
        }
        return won
    }

    // Admin Dashboard & Analytics State
    private val _isAdminDashboardOpen = MutableStateFlow(false)
    val isAdminDashboardOpen: StateFlow<Boolean> = _isAdminDashboardOpen.asStateFlow()

    fun openAdminDashboard() { _isAdminDashboardOpen.value = true }
    fun closeAdminDashboard() { _isAdminDashboardOpen.value = false }

    private val _adminStats = MutableStateFlow(AdminTrafficStats())
    val adminStats: StateFlow<AdminTrafficStats> = _adminStats.asStateFlow()

    private val _managedUsers = MutableStateFlow(
        listOf(
            ManagedUser("u1", "แพรว Ploy (Cinephile)", "Ploy#1234", "VIP", false, 399),
            ManagedUser("u2", "นนท์ Cyber (Gamer)", "Nont#5588", "VIP", false, 149),
            ManagedUser("u3", "มายด์ CineClub", "Mind#9911", "Member", false, 59),
            ManagedUser("u4", "บอทโฆษณา Spammer_01", "Spam#0001", "Member", true, 0)
        )
    )
    val managedUsers: StateFlow<List<ManagedUser>> = _managedUsers.asStateFlow()

    private val _discordAlerts = MutableStateFlow(
        listOf(
            DiscordAlert("d1", "14:38", "🛡️ ระบบ Self-Healing ทำงานปกติ", "Guardian ตรวจสอบความปลอดภัย 24/7 ไม่พบคราบข้อมูลรั่วไหล", "#president-security-logs", "INFO"),
            DiscordAlert("d2", "14:20", "💳 รายได้เข้าบัญชี PromptPay", "เติมเงินแพ็กเกจ Cinema Pro Pass (+฿149)", "#president-revenue", "INFO"),
            DiscordAlert("d3", "14:10", "🔒 บันทึกความปลอดภัย E2EE", "ห้องแชทลับ Room DB เข้ารหัส SQLite เรียบร้อย", "#president-security-logs", "INFO")
        )
    )
    val discordAlerts: StateFlow<List<DiscordAlert>> = _discordAlerts.asStateFlow()

    fun toggleBanUser(userId: String) {
        _managedUsers.update { list ->
            list.map { if (it.id == userId) it.copy(isBanned = !it.isBanned) else it }
        }
    }

    fun promoteUserVip(userId: String) {
        _managedUsers.update { list ->
            list.map { if (it.id == userId) it.copy(role = "VIP President") else it }
        }
    }

    fun sendBroadcastAlert(message: String) {
        sendDiscordAlert(
            title = "📢 ประกาศฉุกเฉินถึงผู้ใช้งานทุกคน",
            message = message,
            severity = "HIGH"
        )
    }

    fun sendDiscordAlert(title: String, message: String, severity: String = "HIGH") {
        val newAlert = DiscordAlert(
            id = UUID.randomUUID().toString(),
            timestamp = "14:45",
            title = title,
            message = message,
            severity = severity
        )
        _discordAlerts.update { listOf(newAlert) + it.take(15) }
    }

    fun testSelfHealing() {
        viewModelScope.launch {
            _adminStats.update { it.copy(serverLatencyMs = 999) }
            delay(1000)
            _adminStats.update { it.copy(serverLatencyMs = 12) }
            sendDiscordAlert(
                title = "⚡ Self-Healing Auto-Restart สำเร็จ",
                message = "ระบบหลังบ้านกู้คืนตัวเองภายใน 1.0 วินาที สถานะ 100% Operational",
                severity = "INFO"
            )
        }
    }

    // Emergency Kill Switch / Wipe All Data
    fun triggerEmergencyKillSwitch() {
        viewModelScope.launch {
            // 1. Wipe Room DB tables
            chatDao.clearAllMessages()
            callDao.clearAllCallRecords()

            // 2. Clear in-memory conversation messages
            _conversations.update { list ->
                list.map { it.copy(messages = emptyList(), lastMessage = "ข้อมูลถูกล้างอย่างปลอดภัย") }
            }

            // 3. End any active call or watch party
            endCall()
            closeMoviePlayer()
            leaveWatchRoom()

            // 4. Send alert to President Discord
            sendDiscordAlert(
                title = "🚨 EMERGENCY KILL SWITCH ถูกสั่งทำงาน!",
                message = "ฐานข้อมูล SQLite Room ทั้งหมดในเครื่องถูกล้างอย่างถาวรโดยคำสั่งประธาน",
                severity = "CRITICAL"
            )

            // 5. Lock vault immediately
            lockVault()
        }
    }
}
