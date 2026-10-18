package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = ObsidianBlack,
    primaryContainer = Color(0xFF003840),
    onPrimaryContainer = NeonCyan,
    secondary = NeonViolet,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF381060),
    onSecondaryContainer = Color(0xFFD6BBFB),
    tertiary = NeonMagenta,
    onTertiary = Color.White,
    background = ObsidianBlack,
    onBackground = TextPrimary,
    surface = ObsidianSurface,
    onSurface = TextPrimary,
    surfaceVariant = ObsidianCard,
    onSurfaceVariant = TextSecondary,
    outline = ObsidianCardBorder,
    error = NeonRed,
    onError = Color.White
)

@Composable
fun PrivacyLiveTheme(
    darkTheme: Boolean = true, // Default to sleek dark cinema mode
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
