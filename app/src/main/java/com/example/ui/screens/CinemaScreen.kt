package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Movie
import com.example.model.StoryItem
import com.example.ui.theme.*

@Composable
fun CinemaScreen(
    movies: List<Movie>,
    stories: List<StoryItem>,
    onSelectMovie: (Movie, asWatchParty: Boolean) -> Unit,
    onOpenStory: (StoryItem) -> Unit,
    onCreateStoryClick: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("ทั้งหมด") }
    var searchQuery by remember { mutableStateOf("") }
    val categories = listOf("ทั้งหมด", "ไซไฟ", "แอ็กชัน", "อวกาศ", "แอนิเมชัน", "ระทึกขวัญ")

    val filteredMovies = remember(movies, selectedCategory, searchQuery) {
        movies.filter { movie ->
            val matchCategory = if (selectedCategory == "ทั้งหมด") true else movie.genres.contains(selectedCategory)
            val matchSearch = if (searchQuery.isBlank()) true else {
                movie.title.contains(searchQuery, ignoreCase = true) ||
                movie.englishTitle.contains(searchQuery, ignoreCase = true)
            }
            matchCategory && matchSearch
        }
    }

    val featuredMovie = movies.firstOrNull { it.isFeatured } ?: movies.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // App Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.linearGradient(listOf(NeonCyan, NeonViolet))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = ObsidianBlack,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "PRIVACY-LIVE",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "โรงภาพยนตร์ & โซเชียลส่วนตัว 4K",
                            color = NeonCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Surface(
                    color = ObsidianCard,
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NeonCyan, NeonViolet)))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(NeonEmerald, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "256-bit E2EE",
                            color = NeonEmerald,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Stories Bar (ลงสตอรี่ & ดูสตอรี่เพื่อน)
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "สตอรี่ & โมเมนต์เพื่อน (Stories)",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(stories) { story ->
                        StoryAvatarItem(
                            story = story,
                            onStoryClick = {
                                if (story.isMyStory) {
                                    onCreateStoryClick()
                                } else {
                                    onOpenStory(story)
                                }
                            }
                        )
                    }
                }
            }
        }

        // Featured Hero Movie (ดูหนัง แนะนำประจำสัปดาห์)
        featuredMovie?.let { hero ->
            item {
                FeaturedMovieCard(
                    movie = hero,
                    onPlayNow = { onSelectMovie(hero, false) },
                    onWatchParty = { onSelectMovie(hero, true) }
                )
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                text = category,
                                color = if (isSelected) ObsidianBlack else TextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NeonCyan,
                            containerColor = ObsidianCard
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) NeonCyan else ObsidianCardBorder
                        )
                    )
                }
            }
        }

        // Watch Party Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = NeonViolet,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ห้องดูพร้อมกัน (Watch Party Live)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Text(
                    text = "จอยได้เลย",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Movies List
        items(filteredMovies) { movie ->
            MovieListItem(
                movie = movie,
                onPlay = { onSelectMovie(movie, false) },
                onWatchParty = { onSelectMovie(movie, true) }
            )
        }
    }
}

@Composable
fun StoryAvatarItem(
    story: StoryItem,
    onStoryClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onStoryClick() }
            .width(68.dp)
    ) {
        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    brush = if (story.isMyStory) {
                        Brush.linearGradient(listOf(NeonCyan, NeonCyanGlow))
                    } else if (!story.isViewed) {
                        Brush.linearGradient(listOf(NeonMagenta, NeonViolet))
                    } else {
                        Brush.linearGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
                    },
                    shape = CircleShape
                )
                .padding(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Brush.linearGradient(story.backgroundGradientColors.map { Color(it) })),
                contentAlignment = Alignment.Center
            ) {
                Text(text = story.stickerEmoji, fontSize = 24.sp)
            }

            if (story.isMyStory) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(NeonCyan, CircleShape)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "เพิ่มสตอรี่",
                        tint = ObsidianBlack,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = story.userName,
            color = if (!story.isViewed && !story.isMyStory) Color.White else TextSecondary,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun FeaturedMovieCard(
    movie: Movie,
    onPlayNow: () -> Unit,
    onWatchParty: () -> Unit
) {
    val context = LocalContext.current
    val resId = remember(movie.posterDrawableName) {
        val id = context.resources.getIdentifier(movie.posterDrawableName, "drawable", context.packageName)
        if (id != 0) id else com.example.R.drawable.ic_launcher_background
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient shadow
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f),
                                Color.Black.copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = NeonCyan,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "ภาพยนตร์ยอดนิยม 4K",
                            color = ObsidianBlack,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "⭐ ${movie.rating}", color = NeonAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = movie.duration, color = TextSecondary, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = movie.title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Text(
                    text = movie.description,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onPlayNow,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = ObsidianBlack)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("ดูหนังทันที", color = ObsidianBlack, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onWatchParty,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Groups, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Watch Party", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieListItem(
    movie: Movie,
    onPlay: () -> Unit,
    onWatchParty: () -> Unit
) {
    val context = LocalContext.current
    val resId = remember(movie.posterDrawableName) {
        val id = context.resources.getIdentifier(movie.posterDrawableName, "drawable", context.packageName)
        if (id != 0) id else com.example.R.drawable.ic_launcher_background
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable { onPlay() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(ObsidianCardBorder, Color.Transparent)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = movie.title,
                modifier = Modifier
                    .size(90.dp, 100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = movie.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${movie.year} • ${movie.duration} • ⭐ ${movie.rating}",
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = movie.description,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = NeonCyan.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clickable { onPlay() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("สตรีมสด", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (movie.watchPartyActive) {
                        Surface(
                            color = NeonViolet.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable { onWatchParty() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Groups, contentDescription = null, tint = NeonViolet, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ดูพร้อมเพื่อน (${movie.currentWatchers})", color = Color(0xFFD6BBFB), fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
