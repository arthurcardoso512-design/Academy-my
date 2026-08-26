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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.UserProfile
import com.example.data.model.ExerciseTemplate
import com.example.data.model.WorkoutCatalog
import com.example.data.model.WorkoutTemplate
import com.example.ui.components.ExerciseDetailDialog
import com.example.ui.components.ExerciseIllustration
import com.example.ui.theme.AppTheme

@Composable
fun WorkoutsCatalogScreen(
    userProfile: UserProfile,
    onStartWorkout: (String) -> Unit,
    onToggleTreinoD: (Boolean) -> Unit
) {
    var selectedExerciseForDetail by remember { mutableStateOf<ExerciseTemplate?>(null) }

    val workouts = listOf(
        WorkoutCatalog.TREINO_A,
        WorkoutCatalog.TREINO_B,
        WorkoutCatalog.TREINO_C,
        WorkoutCatalog.TREINO_D
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .padding(bottom = 80.dp)
            .testTag("workouts_catalog_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Divisão de Treinos",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = AppTheme.colors.textPrimary
                )
                Text(
                    text = "Treino sequencial A → B → C (3x por semana)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.colors.textSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Philosophy Card
        Surface(
            color = AppTheme.colors.primaryContainer,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("💡", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Não se preocupe com o dia da semana. Se treinar Segunda, Quarta e Sexta, ou Terça, Quinta e Sábado, basta seguir a sequência do próximo treino!",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.onPrimaryContainer,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        workouts.forEach { workout ->
            val isTreinoD = workout.code == "D"
            val isLocked = isTreinoD && !userProfile.unlockedTreinoD

            WorkoutCatalogCard(
                workout = workout,
                isLocked = isLocked,
                onStartWorkout = { onStartWorkout(workout.code) },
                onSelectExercise = { selectedExerciseForDetail = it },
                onToggleUnlockTreinoD = { onToggleTreinoD(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (selectedExerciseForDetail != null) {
        ExerciseDetailDialog(
            exercise = selectedExerciseForDetail!!,
            onDismiss = { selectedExerciseForDetail = null }
        )
    }
}

@Composable
fun WorkoutCatalogCard(
    workout: WorkoutTemplate,
    isLocked: Boolean,
    onStartWorkout: () -> Unit,
    onSelectExercise: (ExerciseTemplate) -> Unit,
    onToggleUnlockTreinoD: (Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(!isLocked && workout.code == "A") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("workout_card_${workout.code}"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
        border = BorderStroke(1.dp, AppTheme.colors.border),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isLocked) AppTheme.colors.surfaceVariant else AppTheme.colors.primaryContainer
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLocked) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = AppTheme.colors.textMuted, modifier = Modifier.size(18.dp))
                        } else {
                            Text(
                                text = workout.code,
                                color = AppTheme.colors.onPrimaryContainer,
                                fontWeight = FontWeight.Black,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = workout.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.textPrimary
                        )
                        Text(
                            text = workout.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isLocked) AppTheme.colors.textMuted else AppTheme.colors.blueText
                        )
                    }
                }

                if (!isLocked) {
                    TextButton(onClick = { expanded = !expanded }) {
                        Text(if (expanded) "Recolher" else "Ver exercícios", color = AppTheme.colors.primary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (isLocked) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = AppTheme.colors.surfaceVariant,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, AppTheme.colors.border)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "🔒 Treino opcional (4º dia)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.amberText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Recomendado apenas após 4 a 6 semanas mantendo consistência ininterrupta no esquema A/B/C de 3 treinos semanais.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.colors.textSecondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Desbloquear Treino D", color = AppTheme.colors.textPrimary, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                            Switch(
                                checked = false,
                                onCheckedChange = { onToggleUnlockTreinoD(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = AppTheme.colors.primary)
                            )
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "⏱️ ~${workout.estimatedDurationMin} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.colors.textSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "🏋️ ${workout.exercises.size} exercícios",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.colors.textSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "🏃‍♂️ ${workout.cardioText.take(16)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (expanded) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        workout.exercises.forEachIndexed { index, exercise ->
                            Surface(
                                color = AppTheme.colors.surfaceVariant,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectExercise(exercise) },
                                border = BorderStroke(1.dp, AppTheme.colors.border)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ExerciseIllustration(
                                        exerciseName = exercise.name,
                                        size = 40.dp
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${index + 1}. ${exercise.name}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = AppTheme.colors.textPrimary
                                        )
                                        Text(
                                            text = "${exercise.defaultSets} × ${exercise.minReps}–${exercise.maxReps} | RIR ${exercise.targetRir} | ${exercise.restSecondsMin}s",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = AppTheme.colors.textMuted
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Detalhes",
                                        tint = AppTheme.colors.blueText,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onStartWorkout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Iniciar este Treino", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

