package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.UserProfile
import com.example.ui.theme.AppTheme
import com.example.ui.theme.AppThemeMode

@Composable
fun HabitsSafetyScreen(
    userProfile: UserProfile,
    currentThemeMode: AppThemeMode = AppThemeMode.SYSTEM,
    onSelectTheme: (AppThemeMode) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .padding(bottom = 90.dp)
            .testTag("habits_safety_screen")
    ) {
        Text(
            text = "Hábitos & Configurações",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = AppTheme.colors.textPrimary
        )
        Text(
            text = "Diretrizes de sustentabilidade para perda de gordura, saúde e preferências",
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.colors.textMuted
        )

        Spacer(modifier = Modifier.height(18.dp))

        // THEME PREFERENCES CARD (Dark Mode / Light Mode / Auto)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("theme_settings_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
            border = BorderStroke(1.dp, AppTheme.colors.border),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AppTheme.colors.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = AppTheme.colors.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Aparência do Aplicativo",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.textPrimary
                        )
                        Text(
                            text = "Escolha entre Modo Claro, Modo Escuro ou seguir o sistema",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.colors.textMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Theme Mode Selector Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ThemeOptionButton(
                        title = "Claro",
                        icon = Icons.Default.LightMode,
                        isSelected = currentThemeMode == AppThemeMode.LIGHT,
                        activeColor = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f),
                        onClick = { onSelectTheme(AppThemeMode.LIGHT) }
                    )

                    ThemeOptionButton(
                        title = "Escuro",
                        icon = Icons.Default.DarkMode,
                        isSelected = currentThemeMode == AppThemeMode.DARK,
                        activeColor = Color(0xFF38BDF8),
                        modifier = Modifier.weight(1f),
                        onClick = { onSelectTheme(AppThemeMode.DARK) }
                    )

                    ThemeOptionButton(
                        title = "Sistema",
                        icon = Icons.Default.BrightnessAuto,
                        isSelected = currentThemeMode == AppThemeMode.SYSTEM,
                        activeColor = AppTheme.colors.primary,
                        modifier = Modifier.weight(1f),
                        onClick = { onSelectTheme(AppThemeMode.SYSTEM) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // RED / AMBER SAFETY ALERT SECTION
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.amberContainer),
            border = BorderStroke(1.dp, AppTheme.colors.amberText.copy(alpha = 0.3f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = AppTheme.colors.amberText,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SINAIS DE ALERTA MÉDICO",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = AppTheme.colors.amberText,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Interrompa o treino imediatamente e procure auxílio médico caso sinta:",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.amberText,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                SafetyBullet("Dor, aperto ou desconforto no peito ou irradiando para o braço.")
                SafetyBullet("Tontura intensa, sensação de desmaio ou escurecimento da visão.")
                SafetyBullet("Falta de ar aguda ou palpitações descompassadas.")
                SafetyBullet("Dor articular aguda e em pontada durante qualquer movimento.")

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "⚠️ Este aplicativo é um organizador e diário de treinos e não substitui a orientação presencial de médico ou nutricionista.",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.colors.amberText,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // NON-NEGOTIABLE PILLARS SECTION
        Text(
            text = "Pilares Não Negociáveis do Emagrecimento",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = AppTheme.colors.textPrimary
        )
        Text(
            text = "Para queimar gordura preservando ao máximo a massa muscular:",
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.colors.textMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        PillarCard(
            title = "1. Ingestão de Proteína",
            badge = "1.6 a 2.0 g/kg meta",
            description = "Consuma cerca de 160g a 200g de proteína de alto valor biológico por dia (frango, ovos, peixe, carne magra, whey, iogurte). A proteína protege seus músculos durante o déficit calórico.",
            icon = Icons.Default.Restaurant,
            color = AppTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        PillarCard(
            title = "2. Hidratação Diária",
            badge = "35 a 45 ml/kg",
            description = "Para seu peso atual, beba entre 4 e 5 litros de água diariamente. A hidratação adequada melhora o rendimento no treino, diminui a retenção hídrica e auxilia na saciedade.",
            icon = Icons.Default.LocalDrink,
            color = AppTheme.colors.blueText
        )

        Spacer(modifier = Modifier.height(12.dp))

        PillarCard(
            title = "3. Movimento Diário (NEAT)",
            badge = "7.000 a 10.000 passos",
            description = "Caminhadas diárias e movimentação fora da academia aceleram a queima de gordura sem exigir esforço articular ou fadiga muscular que atrapalhe a musculação.",
            icon = Icons.Default.DirectionsWalk,
            color = AppTheme.colors.amberText
        )

        Spacer(modifier = Modifier.height(12.dp))

        PillarCard(
            title = "4. Sono & Recuperação",
            badge = "7 a 9 horas/noite",
            description = "É durante o sono profundo que ocorre a liberação de GH, síntese de proteínas e regeneração das fibras musculares. Sono ruim eleva o cortisol e aumenta a fome descontrolada.",
            icon = Icons.Default.Bedtime,
            color = AppTheme.colors.purpleText
        )

        Spacer(modifier = Modifier.height(12.dp))

        PillarCard(
            title = "5. Progressão Gradual",
            badge = "Priorize a Técnica",
            description = "Nunca aumente cargas sacrificando a amplitude ou a postura. O objetivo não é bater recordes na primeira semana, mas sim construir um hábito sólido e sustentável.",
            icon = Icons.Default.LocalFireDepartment,
            color = Color(0xFFE11D48)
        )
    }
}

@Composable
private fun ThemeOptionButton(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    activeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) activeColor.copy(alpha = 0.15f) else AppTheme.colors.surfaceVariant,
        border = BorderStroke(
            1.5.dp,
            if (isSelected) activeColor else Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) activeColor else AppTheme.colors.textMuted,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                color = if (isSelected) AppTheme.colors.textPrimary else AppTheme.colors.textMuted
            )
        }
    }
}

@Composable
private fun SafetyBullet(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text("•", color = AppTheme.colors.amberText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, color = AppTheme.colors.amberText, style = MaterialTheme.typography.bodySmall, lineHeight = 18.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PillarCard(
    title: String,
    badge: String,
    description: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
        border = BorderStroke(1.dp, AppTheme.colors.border),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(color.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.textPrimary
                    )
                }

                Surface(
                    color = color.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = badge,
                        color = color,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.colors.textSecondary,
                lineHeight = 19.sp
            )
        }
    }
}

