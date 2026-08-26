package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class AppThemeMode(val title: String, val description: String) {
    SYSTEM("Automático (Sistema)", "Segue a configuração do seu celular"),
    LIGHT("Modo Claro", "Fundo claro e visual limpo"),
    DARK("Modo Escuro", "Tons escuros e alto contraste")
}

data class CustomColors(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val primary: Color,
    val primaryLight: Color,
    val primaryDark: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val heroCardBg: Color,
    val heroCardText: Color,
    val heroCardBorder: Color,
    val heroCardBadge: Color,
    val heroCardBadgeText: Color,
    val blueContainer: Color,
    val blueText: Color,
    val blueBorder: Color,
    val amberContainer: Color,
    val amberText: Color,
    val onAmberContainer: Color,
    val amberBorder: Color,
    val purpleContainer: Color,
    val purpleText: Color,
    val purpleBorder: Color,
    val roseContainer: Color,
    val roseText: Color,
    val roseBorder: Color,
    val navBarBg: Color,
    val isDark: Boolean
)

val LightCustomColors = CustomColors(
    background = Color(0xFFF8F9FA),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF0F0F3),
    border = Color(0xFFE1E2E9),
    textPrimary = Color(0xFF1A1C1E),
    textSecondary = Color(0xFF44474E),
    textMuted = Color(0xFF74777F),
    primary = Color(0xFF10B981),
    primaryLight = Color(0xFF34D399),
    primaryDark = Color(0xFF047857),
    primaryContainer = Color(0xFFECFDF5),
    onPrimaryContainer = Color(0xFF047857),
    heroCardBg = Color(0xFF1A1C1E),
    heroCardText = Color(0xFFFFFFFF),
    heroCardBorder = Color(0xFF334155),
    heroCardBadge = Color(0xFF2E353D),
    heroCardBadgeText = Color(0xFFE2E8F0),
    blueContainer = Color(0xFFEFF6FF),
    blueText = Color(0xFF0284C7),
    blueBorder = Color(0xFFBAE6FD),
    amberContainer = Color(0xFFFFFBEB),
    amberText = Color(0xFFD97706),
    onAmberContainer = Color(0xFF92400E),
    amberBorder = Color(0xFFFDE68A),
    purpleContainer = Color(0xFFFAF5FF),
    purpleText = Color(0xFF7E22CE),
    purpleBorder = Color(0xFFE9D5FF),
    roseContainer = Color(0xFFFEF2F2),
    roseText = Color(0xFFDC2626),
    roseBorder = Color(0xFFFECACA),
    navBarBg = Color(0xFFFFFFFF),
    isDark = false
)

val DarkCustomColors = CustomColors(
    background = Color(0xFF0B1120),
    surface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFF334155),
    border = Color(0xFF334155),
    textPrimary = Color(0xFFF8FAFC),
    textSecondary = Color(0xFFCBD5E1),
    textMuted = Color(0xFF94A3B8),
    primary = Color(0xFF10B981),
    primaryLight = Color(0xFF34D399),
    primaryDark = Color(0xFF059669),
    primaryContainer = Color(0xFF064E3B),
    onPrimaryContainer = Color(0xFFA7F3D0),
    heroCardBg = Color(0xFF0F172A),
    heroCardText = Color(0xFFF8FAFC),
    heroCardBorder = Color(0xFF475569),
    heroCardBadge = Color(0xFF1E293B),
    heroCardBadgeText = Color(0xFFE2E8F0),
    blueContainer = Color(0xFF0C4A6E),
    blueText = Color(0xFF38BDF8),
    blueBorder = Color(0xFF0369A1),
    amberContainer = Color(0xFF451A03),
    amberText = Color(0xFFFBBF24),
    onAmberContainer = Color(0xFFFDE68A),
    amberBorder = Color(0xFF78350F),
    purpleContainer = Color(0xFF3B0764),
    purpleText = Color(0xFFC084FC),
    purpleBorder = Color(0xFF6B21A8),
    roseContainer = Color(0xFF450A0A),
    roseText = Color(0xFFF87171),
    roseBorder = Color(0xFF7F1D1D),
    navBarBg = Color(0xFF1E293B),
    isDark = true
)

val LocalCustomColors = staticCompositionLocalOf { LightCustomColors }

object AppTheme {
    val colors: CustomColors
        @Composable
        get() = LocalCustomColors.current
}



