package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.NavigationTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrivacyLiveTheme {
                MainAppRoot()
            }
        }
    }
}

@Composable
fun MainAppRoot(viewModel: AppViewModel = viewModel()) {
    val isVaultLocked by viewModel.isVaultLocked.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val movies by viewModel.movies.collectAsStateWithLifecycle()
    val activeMovie by viewModel.activeMovie.collectAsStateWithLifecycle()
    val activeWatchRoom by viewModel.activeWatchRoom.collectAsStateWithLifecycle()
    val isMoviePlaying by viewModel.isMoviePlaying.collectAsStateWithLifecycle()
    val moviePlaybackSec by viewModel.moviePlaybackSeconds.collectAsStateWithLifecycle()
    val isWatchParty by viewModel.isWatchParty.collectAsStateWithLifecycle()
    val watchReactions by viewModel.watchReactions.collectAsStateWithLifecycle()
    val selectedQuality by viewModel.selectedQuality.collectAsStateWithLifecycle()
    val selectedSubtitle by viewModel.selectedSubtitle.collectAsStateWithLifecycle()
    val selectedAudio by viewModel.selectedAudio.collectAsStateWithLifecycle()

    val clips by viewModel.clips.collectAsStateWithLifecycle()
    val clipComments by viewModel.clipComments.collectAsStateWithLifecycle()
    val activeCommentsClipId by viewModel.activeCommentsClipId.collectAsStateWithLifecycle()

    val stories by viewModel.stories.collectAsStateWithLifecycle()
    val activeStory by viewModel.activeStory.collectAsStateWithLifecycle()

    val conversations by viewModel.conversations.collectAsStateWithLifecycle()
    val activeChatId by viewModel.activeChatId.collectAsStateWithLifecycle()
    val isRecordingVoice by viewModel.isRecordingVoice.collectAsStateWithLifecycle()
    val recordingDurationSec by viewModel.recordingDurationSec.collectAsStateWithLifecycle()

    val activeCall by viewModel.activeCall.collectAsStateWithLifecycle()

    // Social-Fi & Admin state
    val isSocialFiOpen by viewModel.isSocialFiOpen.collectAsStateWithLifecycle()
    val isAdminDashboardOpen by viewModel.isAdminDashboardOpen.collectAsStateWithLifecycle()
    val balanceCoins by viewModel.balanceCoins.collectAsStateWithLifecycle()
    val balanceGems by viewModel.balanceGems.collectAsStateWithLifecycle()
    val vipTier by viewModel.vipTier.collectAsStateWithLifecycle()
    val adminStats by viewModel.adminStats.collectAsStateWithLifecycle()
    val managedUsers by viewModel.managedUsers.collectAsStateWithLifecycle()
    val discordAlerts by viewModel.discordAlerts.collectAsStateWithLifecycle()

    var showCreateStoryDialog by remember { mutableStateOf(false) }
    var showPostClipDialog by remember { mutableStateOf(false) }

    // Biometric & PIN Security Vault Lock Screen
    if (isVaultLocked) {
        SecurityLockScreen(
            title = "ระบบความปลอดภัย PRIVACY-LIVE",
            subtitle = "สแกนลายนิ้วมือ หรือใส่รหัส PIN 6 หลักเพื่อเข้าใช้งาน",
            correctPin = "123456",
            onUnlocked = { viewModel.unlockVault() },
            onEmergencyWipe = { viewModel.triggerEmergencyKillSwitch() },
            onFailedAttemptAlert = { alertMsg ->
                viewModel.sendDiscordAlert("🚨 ตรวจพบการพยายามสุ่มรหัส PIN", alertMsg, "CRITICAL")
            }
        )
        return
    }

    // Back button handling
    BackHandler(enabled = isAdminDashboardOpen || isSocialFiOpen || activeMovie != null || activeStory != null || activeChatId != null || (activeCall != null && !activeCall!!.isMinimized)) {
        when {
            isAdminDashboardOpen -> viewModel.closeAdminDashboard()
            isSocialFiOpen -> viewModel.closeSocialFi()
            activeMovie != null -> viewModel.closeMoviePlayer()
            activeStory != null -> viewModel.closeStory()
            activeCall != null && !activeCall!!.isMinimized -> viewModel.toggleMinimizeCall()
            activeChatId != null -> viewModel.closeChat()
        }
    }

    Scaffold(
        containerColor = ObsidianBlack,
        bottomBar = {
            if (!isAdminDashboardOpen && !isSocialFiOpen && activeMovie == null && (activeCall == null || activeCall!!.isMinimized)) {
                NavigationBar(
                    containerColor = ObsidianSurface,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    NavigationBarItem(
                        selected = currentTab == NavigationTab.CINEMA,
                        onClick = { viewModel.selectTab(NavigationTab.CINEMA) },
                        icon = { Icon(Icons.Default.Movie, contentDescription = "ดูหนัง") },
                        label = { Text("ดูหนัง", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ObsidianBlack,
                            selectedTextColor = NeonCyan,
                            indicatorColor = NeonCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )

                    NavigationBarItem(
                        selected = currentTab == NavigationTab.CLIPS,
                        onClick = { viewModel.selectTab(NavigationTab.CLIPS) },
                        icon = { Icon(Icons.Default.VideoLibrary, contentDescription = "คลิปสั้น") },
                        label = { Text("คลิปสั้น", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ObsidianBlack,
                            selectedTextColor = NeonMagenta,
                            indicatorColor = NeonMagenta,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )

                    NavigationBarItem(
                        selected = currentTab == NavigationTab.STORIES,
                        onClick = { viewModel.selectTab(NavigationTab.STORIES) },
                        icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "สตอรี่") },
                        label = { Text("สตอรี่", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ObsidianBlack,
                            selectedTextColor = NeonViolet,
                            indicatorColor = Color(0xFFD6BBFB),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )

                    NavigationBarItem(
                        selected = currentTab == NavigationTab.CHAT,
                        onClick = { viewModel.selectTab(NavigationTab.CHAT) },
                        icon = {
                            BadgedBox(badge = {
                                val unread = conversations.sumOf { it.unreadCount }
                                if (unread > 0) {
                                    Badge(containerColor = NeonCyan) {
                                        Text("$unread", color = ObsidianBlack)
                                    }
                                }
                            }) {
                                Icon(Icons.Default.Chat, contentDescription = "แชทลับ")
                            }
                        },
                        label = { Text("แชทลับ", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ObsidianBlack,
                            selectedTextColor = NeonCyan,
                            indicatorColor = NeonCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )

                    NavigationBarItem(
                        selected = currentTab == NavigationTab.PROFILE,
                        onClick = { viewModel.selectTab(NavigationTab.PROFILE) },
                        icon = { Icon(Icons.Default.Security, contentDescription = "ความปลอดภัย") },
                        label = { Text("เซฟ/โปรไฟล์", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ObsidianBlack,
                            selectedTextColor = NeonEmerald,
                            indicatorColor = NeonEmerald,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isAdminDashboardOpen -> {
                    AdminDashboardScreen(
                        stats = adminStats,
                        users = managedUsers,
                        discordAlerts = discordAlerts,
                        onBack = { viewModel.closeAdminDashboard() },
                        onToggleBanUser = { id -> viewModel.toggleBanUser(id) },
                        onPromoteUserVip = { id -> viewModel.promoteUserVip(id) },
                        onSendBroadcastAlert = { msg -> viewModel.sendBroadcastAlert(msg) },
                        onSendTestDiscordAlert = {
                            viewModel.sendDiscordAlert(
                                "👑 ทดสอบการแจ้งเตือน Discord ประธาน",
                                "ระบบตรวจสอบความปลอดภัยพร้อมใช้งาน เชื่อมต่อ Webhook เรียบร้อย",
                                "INFO"
                            )
                        },
                        onSelfHealingTest = { viewModel.testSelfHealing() }
                    )
                }
                isSocialFiOpen -> {
                    SocialFiScreen(
                        balanceCoins = balanceCoins,
                        balanceGems = balanceGems,
                        vipTier = vipTier,
                        linkedDiscordTag = viewModel.linkedDiscordTag,
                        onTopUpSuccess = { coins, gems, tier ->
                            viewModel.topUp(coins, gems, tier)
                        },
                        onPlayGacha = { cost ->
                            viewModel.playGacha(cost)
                        }
                    )
                }
                else -> {
                    when (currentTab) {
                        NavigationTab.CINEMA -> {
                            CinemaScreen(
                                movies = movies,
                                stories = stories,
                                onSelectMovie = { movie, asWatchParty ->
                                    if (asWatchParty) {
                                        viewModel.createWatchRoom(movie, "ห้องดูหนัง: ${movie.title}")
                                    } else {
                                        viewModel.playMovie(movie, false)
                                    }
                                },
                                onOpenStory = { story -> viewModel.openStory(story) },
                                onCreateStoryClick = { showCreateStoryDialog = true }
                            )
                        }
                        NavigationTab.CLIPS -> {
                            ClipsScreen(
                                clips = clips,
                                commentsMap = clipComments,
                                activeCommentsClipId = activeCommentsClipId,
                                onToggleLike = { clipId -> viewModel.toggleLikeClip(clipId) },
                                onOpenComments = { clipId -> viewModel.openComments(clipId) },
                                onCloseComments = { viewModel.closeComments() },
                                onAddComment = { clipId, text -> viewModel.addClipComment(clipId, text) },
                                onOpenPostDialog = { showPostClipDialog = true }
                            )
                        }
                        NavigationTab.STORIES -> {
                            StoryScreen(
                                stories = stories,
                                onOpenStory = { story -> viewModel.openStory(story) },
                                onCreateStoryClick = { showCreateStoryDialog = true }
                            )
                        }
                        NavigationTab.CHAT -> {
                            val activeConv = conversations.firstOrNull { it.id == activeChatId }
                            if (activeConv != null) {
                                ChatDetailScreen(
                                    conversation = activeConv,
                                    isRecordingVoice = isRecordingVoice,
                                    recordingDurationSeconds = recordingDurationSec,
                                    onBack = { viewModel.closeChat() },
                                    onStartCall = { name, callType -> viewModel.startCall(name, callType) },
                                    onSendText = { text -> viewModel.sendTextMessage(activeConv.id, text) },
                                    onStartVoiceRecord = { viewModel.startVoiceRecording() },
                                    onStopAndSendVoiceRecord = { viewModel.stopAndSendVoiceRecording(activeConv.id) },
                                    onCancelVoiceRecord = { viewModel.cancelVoiceRecording() },
                                    onTogglePlayVoice = { msgId -> viewModel.togglePlayVoiceMessage(activeConv.id, msgId) }
                                )
                            } else {
                                ChatListScreen(
                                    conversations = conversations,
                                    stories = stories,
                                    onSelectChat = { chatId -> viewModel.openChat(chatId) },
                                    onOpenStory = { story -> viewModel.openStory(story) },
                                    onLockVault = { viewModel.lockVault() }
                                )
                            }
                        }
                        NavigationTab.PROFILE -> {
                            ProfileScreen(
                                onTriggerKillSwitch = { viewModel.triggerEmergencyKillSwitch() },
                                onLockAppNow = { viewModel.lockVault() },
                                onOpenSocialFi = { viewModel.openSocialFi() },
                                onOpenAdminDashboard = { viewModel.openAdminDashboard() }
                            )
                        }
                    }
                }
            }

            // FULL CINEMA VIDEO PLAYER OVERLAY (With Watch Room Co-watching & Text Chat)
            activeMovie?.let { movie ->
                CinemaPlayerOverlay(
                    movie = movie,
                    isPlaying = isMoviePlaying,
                    playbackSeconds = moviePlaybackSec,
                    isWatchParty = isWatchParty,
                    watchRoom = activeWatchRoom,
                    reactions = watchReactions,
                    selectedQuality = selectedQuality,
                    selectedSubtitle = selectedSubtitle,
                    selectedAudio = selectedAudio,
                    onPlayPause = { viewModel.toggleMoviePlayPause() },
                    onRewind = { viewModel.rewindMovie(10) },
                    onForward = { viewModel.forwardMovie(10) },
                    onSeek = { targetSec -> viewModel.seekMovieTo(targetSec) },
                    onSendReaction = { emoji -> viewModel.sendWatchReaction(emoji) },
                    onSendRoomMessage = { msg -> viewModel.sendRoomChatMessage(msg) },
                    onClose = { viewModel.closeMoviePlayer() },
                    onSelectQuality = { q -> viewModel.setQuality(q) },
                    onSelectSubtitle = { s -> viewModel.setSubtitle(s) },
                    onSelectAudio = { a -> viewModel.setAudio(a) }
                )
            }

            // FULL SCREEN STORY VIEWER
            activeStory?.let { story ->
                StoryViewerModal(
                    story = story,
                    onClose = { viewModel.closeStory() },
                    onReplyToStory = { reply ->
                        val firstChat = conversations.firstOrNull()?.id ?: "chat_1"
                        viewModel.sendTextMessage(firstChat, "ตอบกลับสตอรี่: $reply")
                    }
                )
            }

            // ACTIVE CALL OVERLAY (Voice/Video Call & Screen Sharing)
            activeCall?.let { call ->
                CallOverlay(
                    call = call,
                    onEndCall = { viewModel.endCall() },
                    onToggleMute = { viewModel.toggleCallMute() },
                    onToggleSpeaker = { viewModel.toggleCallSpeaker() },
                    onToggleVideo = { viewModel.toggleCallVideo() },
                    onToggleCameraSwitch = { viewModel.toggleCameraSwitch() },
                    onToggleScreenShare = { viewModel.toggleScreenShare() },
                    onToggleMinimize = { viewModel.toggleMinimizeCall() }
                )
            }
        }
    }

    // Modal: Create Story Dialog
    if (showCreateStoryDialog) {
        CreateStoryDialog(
            onDismiss = { showCreateStoryDialog = false },
            onPostStory = { caption, emoji, music ->
                viewModel.postNewStory(caption, emoji, music)
                showCreateStoryDialog = false
            }
        )
    }

    // Modal: Post Clip Dialog
    if (showPostClipDialog) {
        PostClipDialog(
            onDismiss = { showPostClipDialog = false },
            onPostClip = { title, tags, privacy ->
                viewModel.postNewClip(title, tags, privacy)
                showPostClipDialog = false
            }
        )
    }
}
