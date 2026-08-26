package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.entity.UserProfile
import com.example.data.entity.WeightRecord
import com.example.data.entity.WorkoutSession
import com.example.data.model.WorkoutCatalog
import com.example.data.model.WorkoutTemplate
import com.example.ui.components.MilestoneCelebrationDialog
import com.example.ui.components.ThemeSelectionDialog
import com.example.ui.theme.AppTheme
import com.example.ui.theme.AppThemeMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    userProfile: UserProfile,
    nextWorkout: WorkoutTemplate,
    recentSessions: List<WorkoutSession>,
    weightRecords: List<WeightRecord>,
    newMilestone: Float?,
    currentThemeMode: AppThemeMode = AppThemeMode.SYSTEM,
    onSelectTheme: (AppThemeMode) -> Unit = {},
    onToggleTheme: () -> Unit = {},
    onStartWorkout: (String) -> Unit,
    onNavigateToWorkouts: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToMeasurements: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToCheckIn: () -> Unit,
    onNavigateToHabits: () -> Unit,
    onRecordWeightClick: () -> Unit,
    onDismissMilestone: () -> Unit
) {
    var showCheckInDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    val initialWeight = userProfile.initialWeightKg
    val currentWeight = userProfile.currentWeightKg
    val targetWeight = userProfile.targetWeightKg

    // Calculate percentage of path traveled
    val totalWeightToLose = (initialWeight - targetWeight).coerceAtLeast(0.1f)
    val lostSoFar = (initialWeight - currentWeight).coerceAtLeast(0f)
    val progressPercent = ((lostSoFar / totalWeightToLose) * 100f).coerceIn(0f, 100f)
    val animatedProgress by animateFloatAsState(targetValue = progressPercent / 100f, label = "WeightProgress")

    // Weekly consistency count (completed in past 7 days)
    val now = System.currentTimeMillis()
    val sevenDaysAgo = now - (7L * 24 * 3600 * 1000)
    val completedThisWeek = recentSessions.count { it.status == "CONCLUIDO" && it.dateMillis >= sevenDaysAgo }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
            .testTag("home_screen")
    ) {
        // Top Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.background)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Olá, ${userProfile.name}!",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = AppTheme.colors.textPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "👋", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Vamos manter a consistência hoje?",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.colors.textSecondary
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Dark Mode / Theme Selector Button
                        Surface(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .clickable { showThemeDialog = true }
                                .testTag("theme_toggle_button"),
                            shape = CircleShape,
                            color = AppTheme.colors.surface,
                            border = BorderStroke(1.dp, AppTheme.colors.border)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = when (currentThemeMode) {
                                        AppThemeMode.DARK -> Icons.Default.DarkMode
                                        AppThemeMode.LIGHT -> Icons.Default.LightMode
                                        AppThemeMode.SYSTEM -> Icons.Default.BrightnessAuto
                                    },
                                    contentDescription = "Alternar Tema",
                                    tint = when (currentThemeMode) {
                                        AppThemeMode.DARK -> Color(0xFF38BDF8)
                                        AppThemeMode.LIGHT -> Color(0xFFF59E0B)
                                        AppThemeMode.SYSTEM -> AppTheme.colors.primary
                                    },
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Avatar Circle
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(AppTheme.colors.primaryContainer)
                                .border(1.dp, AppTheme.colors.primary.copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userProfile.name.take(1).uppercase(),
                                color = AppTheme.colors.onPrimaryContainer,
                                fontWeight = FontWeight.Black,
                                fontSize = 17.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Core Mindset Banner
                Surface(
                    color = AppTheme.colors.primaryContainer,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "⚡", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CONSISTÊNCIA → DESEMPENHO → MEDIDAS → PESO",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = AppTheme.colors.onPrimaryContainer,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {

            // 1. PROGRESS CARD (Adaptive Surface Card)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToProgress() }
                    .testTag("progress_card"),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
                border = BorderStroke(1.dp, AppTheme.colors.border),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "PROGRESSO DE PESO",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.textMuted,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = String.format(Locale.getDefault(), "%.1f", currentWeight),
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Black,
                                    color = AppTheme.colors.textPrimary
                                )
                                Text(
                                    text = " kg",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = AppTheme.colors.textMuted,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }

                        Surface(
                            color = AppTheme.colors.primaryContainer,
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "-${String.format(Locale.getDefault(), "%.1f", lostSoFar)} kg total",
                                color = AppTheme.colors.onPrimaryContainer,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Progress Bar Visual
                    Column {
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = AppTheme.colors.primary,
                            trackColor = AppTheme.colors.surfaceVariant,
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${initialWeight.toInt()} kg (Início)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.textMuted
                            )
                            Text(
                                text = "${progressPercent.toInt()}% do caminho",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.primary
                            )
                            Text(
                                text = "${targetWeight.toInt()} kg (Meta)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.textMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. HERO CARD "PRÓXIMO TREINO" (Sleek Onyx Hero Card)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("next_workout_card"),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = AppTheme.colors.heroCardBg),
                border = BorderStroke(1.dp, AppTheme.colors.heroCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = AppTheme.colors.heroCardBadge,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "PRÓXIMO TREINO",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = AppTheme.colors.heroCardBadgeText,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                letterSpacing = 0.8.sp
                            )
                        }

                        Text(
                            text = "Hoje • Sequência A → B → C",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.primaryLight
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = nextWorkout.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = AppTheme.colors.heroCardText
                    )

                    Text(
                        text = nextWorkout.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.colors.textMuted,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(AppTheme.colors.primaryLight))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${nextWorkout.exercises.size} Exercícios",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.colors.heroCardBadgeText
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF38BDF8)))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "~${nextWorkout.estimatedDurationMin} minutos",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.colors.heroCardBadgeText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Sleek High-Contrast Action Button
                    Button(
                        onClick = { showCheckInDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("start_workout_button"),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colors.primary
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "COMEÇAR TREINO",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. STATS HIGHLIGHTS GRID (Streak + Steps)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Streak Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToHistory() },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
                    border = BorderStroke(1.dp, AppTheme.colors.border)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(AppTheme.colors.amberContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🔥", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "STREAK",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.textMuted,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = "$completedThisWeek/3 sem",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Black,
                                color = AppTheme.colors.textPrimary
                            )
                        }
                    }
                }

                // Steps Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToCheckIn() },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
                    border = BorderStroke(1.dp, AppTheme.colors.border)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(AppTheme.colors.blueContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🚶", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "PASSOS",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.textMuted,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = "8.420 /dia",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Black,
                                color = AppTheme.colors.textPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. WEEKLY CALENDAR & CONSISTENCY
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToHistory() },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
                border = BorderStroke(1.dp, AppTheme.colors.border)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Calendário Semanal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.textPrimary
                        )
                        Text(
                            text = "Histórico →",
                            style = MaterialTheme.typography.labelMedium,
                            color = AppTheme.colors.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Days row (SEG TER QUA QUI SEX SÁB DOM)
                    val days = listOf("SEG", "TER", "QUA", "QUI", "SEX", "SÁB", "DOM")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        days.forEachIndexed { index, dayName ->
                            val isWorkoutDone = (index == 0 || index == 2 || (index == 4 && completedThisWeek >= 3))
                            val isPlannedToday = (index == 4 && completedThisWeek < 3)

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = dayName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AppTheme.colors.textMuted,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isWorkoutDone -> AppTheme.colors.primary
                                                isPlannedToday -> Color(0xFFF59E0B)
                                                else -> AppTheme.colors.surfaceVariant
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when {
                                            isWorkoutDone -> "✓"
                                            isPlannedToday -> "A"
                                            else -> "•"
                                        },
                                        color = if (isWorkoutDone || isPlannedToday) Color.White else AppTheme.colors.textMuted,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(AppTheme.colors.primary))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Concluído", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.textMuted)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Planejado", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.textMuted)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(AppTheme.colors.surfaceVariant))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Descanso", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.textMuted)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. QUICK ACTION SHORTCUTS
            Text(
                text = "Acesso Rápido",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "+ Registrar Peso",
                    subtitle = "Acompanhe média",
                    icon = Icons.Default.MonitorWeight,
                    tint = AppTheme.colors.blueText,
                    containerColor = AppTheme.colors.blueContainer,
                    modifier = Modifier.weight(1f),
                    onClick = onRecordWeightClick
                )

                QuickActionCard(
                    title = "Check-in Semanal",
                    subtitle = "Energia, sono e dor",
                    icon = Icons.Default.CheckCircle,
                    tint = AppTheme.colors.onPrimaryContainer,
                    containerColor = AppTheme.colors.primaryContainer,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToCheckIn
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "Medidas 📏",
                    subtitle = "Cintura, abdômen",
                    icon = Icons.Default.Straighten,
                    tint = AppTheme.colors.amberText,
                    containerColor = AppTheme.colors.amberContainer,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToMeasurements
                )

                QuickActionCard(
                    title = "Hábitos & Dicas",
                    subtitle = "Proteína, água",
                    icon = Icons.Default.Restaurant,
                    tint = AppTheme.colors.purpleText,
                    containerColor = AppTheme.colors.purpleContainer,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToHabits
                )
            }
        }
    }

    // CHECK-IN POPUP DIALOG
    if (showCheckInDialog) {
        WorkoutCheckInDialog(
            workout = nextWorkout,
            onDismiss = { showCheckInDialog = false },
            onConfirmCheckIn = {
                showCheckInDialog = false
                onStartWorkout(nextWorkout.code)
            }
        )
    }

    // THEME SELECTION DIALOG
    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = currentThemeMode,
            onSelectTheme = { mode ->
                onSelectTheme(mode)
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    // MILESTONE POPUP
    if (newMilestone != null) {
        MilestoneCelebrationDialog(
            milestoneWeightKg = newMilestone,
            onDismiss = onDismissMilestone
        )
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    tint: Color,
    containerColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
        border = BorderStroke(1.dp, AppTheme.colors.border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.textPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.colors.textMuted
            )
        }
    }
}

@Composable
fun WorkoutCheckInDialog(
    workout: WorkoutTemplate,
    onDismiss: () -> Unit,
    onConfirmCheckIn: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("workout_checkin_dialog"),
            border = BorderStroke(1.dp, AppTheme.colors.border)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🏋️", fontSize = 30.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Vamos começar?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = AppTheme.colors.textPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = workout.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.primary
                )

                Text(
                    text = "\"${workout.subtitle}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.colors.textSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = AppTheme.colors.background,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, AppTheme.colors.border)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Estimativa", color = AppTheme.colors.textMuted, style = MaterialTheme.typography.labelSmall)
                            Text("${workout.estimatedDurationMin} min", color = AppTheme.colors.textPrimary, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Volume", color = AppTheme.colors.textMuted, style = MaterialTheme.typography.labelSmall)
                            Text("${workout.exercises.size} exercícios", color = AppTheme.colors.textPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "O aplicativo registrará automaticamente seu horário de início, histórico de cargas e descanso entre séries.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.textMuted,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onConfirmCheckIn,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("confirm_checkin_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary)
                ) {
                    Text(
                        text = "FAZER CHECK-IN",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(onClick = onDismiss) {
                    Text("Agora não", color = AppTheme.colors.textMuted)
                }
            }
        }
    }
}

