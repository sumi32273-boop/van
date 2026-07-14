package com.example.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val NeonCyan = Color(0xFF00FFC2)
val NeonBlue = Color(0xFF0080FF)
val NeonPurple = Color(0xFF9D4EDD)
val NeonPink = Color(0xFFFF007F)
val DeepSpace = Color(0xFF050505)
val SurfaceDark = Color(0xFF121214)
val SurfaceCard = Color(0xFF18181B)
val TextPrimary = Color(0xFFF5F5F5)
val TextSecondary = Color(0xFFA1A1AA)
val AccentGreen = Color(0xFF10B981)

val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    secondary = NeonBlue,
    tertiary = NeonPurple,
    background = DeepSpace,
    surface = SurfaceDark,
    onPrimary = DeepSpace,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary
)

