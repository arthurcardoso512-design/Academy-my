package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.ExerciseSetLog
import com.example.data.model.ExerciseTemplate
import com.example.data.model.WorkoutTemplate
import com.example.ui.components.ExerciseDetailDialog
import com.example.ui.components.ExerciseIllustration
import com.example.ui.components.RestTimerDialog
import com.example.ui.components.RirExplainerDialog
import com.example.ui.theme.AppTheme
import com.example.ui.viewmodel.ActiveWorkoutUiState
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun WorkoutActiveScreen(
    state: ActiveWorkoutUiState,
    onUpdateSetLog: (Long, Float, Int, Int) -> Unit,
    onCompleteSet: (Long) -> Unit,
    onNextExercise: () -> Unit,
    onJumpToExercise: (Int) -> Unit,
    onSkipExercise: (String) -> Unit,
    onFinishWorkout: (Int, String) -> Unit,
    onCancelWorkout: () -> Unit,
    onDismissRestTimer: () -> Unit,
    onOpenRirExplainer: () -> Unit,
    onDismissRirExplainer: () -> Unit,
    onOpenExerciseDetail: () -> Unit,
    onDismissExerciseDetail: () -> Unit,
    onOpenSkipDialog: () -> Unit,
    onDismissSkipDialog: () -> Unit,
    onNavigateToCheckIn: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val template = state.workoutTemplate ?: return
    val session = state.activeSession

    // Elapsed timer calculation
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    LaunchedEffect(session?.startTimeMillis) {
        val start = session?.startTimeMillis ?: System.currentTimeMillis()
        while (true) {
            val now = System.currentTimeMillis()
            elapsedSeconds = maxOf(0, ((now - start) / 1000).toInt())
            delay(1000L)
        }
    }

    val elapsedMinutes = elapsedSeconds / 60
    val elapsedSecs = elapsedSeconds % 60
    val timerText = String.format("%02d:%02d", elapsedMinutes, elapsedSecs)

    // Completion Celebration Screen View
    if (state.isWorkoutFinished) {
        WorkoutCompletionCelebrationScreen(
            template = template,
            durationMin = maxOf(1, elapsedMinutes),
            completedExercises = state.sessionLogs.filter { it.completed }.map { it.exerciseName }.distinct().size,
            totalExercises = template.exercises.size,
            completedSets = state.sessionLogs.count { it.completed },
            totalSets = state.sessionLogs.size,
            onFinalize = {
                onFinishWorkout(8, "Treino finalizado com sucesso!")
                onNavigateToHome()
            },
            onGoToCheckIn = {
                onFinishWorkout(8, "Treino finalizado com sucesso!")
                onNavigateToCheckIn()
            },
            onGoToHome = {
                onFinishWorkout(8, "Treino finalizado com sucesso!")
                onNavigateToHome()
            }
        )
        return
    }

    val currentExIndex = state.currentExerciseIndex.coerceIn(0, template.exercises.size - 1)
    val currentExercise = template.exercises[currentExIndex]
    val currentExLogs = state.sessionLogs.filter { it.exerciseName == currentExercise.name }

    val completedExercisesCount = template.exercises.count { ex ->
        val logs = state.sessionLogs.filter { it.exerciseName == ex.name }
        logs.isNotEmpty() && logs.all { it.completed || it.skipped }
    }
    val workoutProgress = (completedExercisesCount.toFloat() / template.exercises.size.toFloat()).coerceIn(0f, 1f)

    var showSmartListModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .testTag("workout_active_screen")
    ) {
        // TOP APP BAR
        Surface(
            color = AppTheme.colors.surface,
            shadowElevation = 2.dp,
            border = BorderStroke(1.dp, AppTheme.colors.border)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onCancelWorkout) {
                            Icon(Icons.Default.Close, contentDescription = "Cancelar treino", tint = AppTheme.colors.textMuted)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = template.title.uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = AppTheme.colors.textPrimary
                            )
                            Text(
                                text = template.subtitle,
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.colors.blueText,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Live Timer Badge
                    Surface(
                        color = AppTheme.colors.primaryContainer,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Timer,
                                contentDescription = null,
                                tint = AppTheme.colors.onPrimaryContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = timerText,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.onPrimaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Overall Workout Progress bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Progresso: ${completedExercisesCount + 1} de ${template.exercises.size} exercícios",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.colors.textMuted,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${(workoutProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.primary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { workoutProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = AppTheme.colors.primary,
                    trackColor = AppTheme.colors.surfaceVariant
                )
            }
        }

        // SMART HORIZONTAL EXERCISE TRACKER ("Você está aqui")
        Surface(
            color = AppTheme.colors.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                template.exercises.forEachIndexed { index, ex ->
                    val logs = state.sessionLogs.filter { it.exerciseName == ex.name }
                    val isCompleted = logs.isNotEmpty() && logs.all { it.completed || it.skipped }
                    val isCurrent = index == currentExIndex

                    Surface(
                        color = when {
                            isCurrent -> AppTheme.colors.primary
                            isCompleted -> AppTheme.colors.primaryContainer
                            else -> AppTheme.colors.surface
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            when {
                                isCurrent -> AppTheme.colors.primary
                                isCompleted -> AppTheme.colors.primary.copy(alpha = 0.4f)
                                else -> AppTheme.colors.border
                            }
                        ),
                        modifier = Modifier
                            .clickable { onJumpToExercise(index) }
                            .testTag("step_exercise_$index")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when {
                                    isCompleted -> "✓"
                                    isCurrent -> "→"
                                    else -> "○"
                                },
                                color = if (isCurrent) Color.White else if (isCompleted) AppTheme.colors.onPrimaryContainer else AppTheme.colors.textMuted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = ex.name.take(14),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                color = if (isCurrent) Color.White else if (isCompleted) AppTheme.colors.onPrimaryContainer else AppTheme.colors.textSecondary
                            )
                        }
                    }
                }
            }
        }

        // MAIN ACTIVE EXERCISE CONTAINER
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Exercise Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
                border = BorderStroke(1.dp, AppTheme.colors.border),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Illustration badge (Clickable to open big view)
                        Box(
                            modifier = Modifier
                                .clickable { onOpenExerciseDetail() }
                                .testTag("exercise_illustration_box")
                        ) {
                            ExerciseIllustration(
                                exerciseName = currentExercise.name,
                                size = 72.dp
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentExercise.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = AppTheme.colors.textPrimary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${currentExercise.defaultSets} séries × ${currentExercise.minReps}–${currentExercise.maxReps} reps",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.blueText
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // RIR Badge with ? click
                                Surface(
                                    color = AppTheme.colors.primaryContainer,
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.3f)),
                                    modifier = Modifier.clickable { onOpenRirExplainer() }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "RIR ${currentExercise.targetRir}",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = AppTheme.colors.onPrimaryContainer
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            Icons.Default.HelpOutline,
                                            contentDescription = "O que é RIR?",
                                            tint = AppTheme.colors.onPrimaryContainer,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "Descanso: ${currentExercise.restSecondsMin}s",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AppTheme.colors.textMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Foco: ${currentExercise.muscleFocus}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.colors.purpleText,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "\"${currentExercise.shortDescription}\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.colors.textSecondary,
                        lineHeight = 18.sp
                    )

                    if (currentExercise.alertWarning != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = AppTheme.colors.amberContainer,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, AppTheme.colors.amberText.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = AppTheme.colors.amberText, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(currentExercise.alertWarning, color = AppTheme.colors.onAmberContainer, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // PREVIOUS SESSION REFERENCE CARD
            val prevLogs = state.previousExerciseLogs
            if (prevLogs.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
                    border = BorderStroke(1.dp, AppTheme.colors.border)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Sessão anterior:",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.colors.textMuted,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        val prevRepsList = prevLogs.map { it.reps }.joinToString("/")
                        val prevLoad = prevLogs.firstOrNull()?.weightKg ?: 0f
                        Text(
                            text = "${prevLoad.toInt()} kg — $prevRepsList",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.colors.textPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // FEEDBACK & ALERTS
            if (state.performanceFeedback != null) {
                Surface(
                    color = AppTheme.colors.primaryContainer,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "📈", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = state.performanceFeedback,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.colors.onPrimaryContainer,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (state.showLargeLoadAlert) {
                Surface(
                    color = AppTheme.colors.amberContainer,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, AppTheme.colors.amberText.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = AppTheme.colors.amberText)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Você aumentou bastante a carga. Confirme se a execução continua confortável.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.colors.onAmberContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // SETS TABLE / INPUT ROWS
            Text(
                text = "Séries Programadas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            currentExLogs.forEach { setLog ->
                SetLogRow(
                    setLog = setLog,
                    onUpdateValues = { weight, reps, rir ->
                        onUpdateSetLog(setLog.id, weight, reps, rir)
                    },
                    onComplete = {
                        onCompleteSet(setLog.id)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            val allCurrentSetsDone = currentExLogs.isNotEmpty() && currentExLogs.all { it.completed || it.skipped }

            // ACTION BUTTONS
            if (allCurrentSetsDone) {
                // Exercise Completed Banner
                Surface(
                    color = AppTheme.colors.primaryContainer,
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Exercício concluído!", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = AppTheme.colors.onPrimaryContainer)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "🔥", fontSize = 22.sp)
                        }

                        val nextEx = template.exercises.getOrNull(currentExIndex + 1)
                        if (nextEx != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Próximo: ${nextEx.name.uppercase()}",
                                style = MaterialTheme.typography.labelMedium,
                                color = AppTheme.colors.blueText,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onNextExercise,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("next_exercise_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary)
                ) {
                    Text(
                        text = if (currentExIndex < template.exercises.size - 1) "PRÓXIMO EXERCÍCIO →" else "CONCLUIR TREINO 🎉",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onOpenSkipDialog,
                        modifier = Modifier.testTag("skip_exercise_button")
                    ) {
                        Text("Pular exercício", color = AppTheme.colors.textMuted, style = MaterialTheme.typography.bodySmall)
                    }

                    OutlinedButton(
                        onClick = onNextExercise,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Avançar →", color = AppTheme.colors.textPrimary, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // DIALOGS & OVERLAYS
    if (state.isRestTimerVisible) {
        RestTimerDialog(
            initialSeconds = state.restTimerSeconds,
            onDismiss = onDismissRestTimer,
            onFinished = onDismissRestTimer
        )
    }

    if (state.isRirExplainerVisible) {
        RirExplainerDialog(onDismiss = onDismissRirExplainer)
    }

    if (state.isExerciseDetailVisible) {
        ExerciseDetailDialog(
            exercise = currentExercise,
            onDismiss = onDismissExerciseDetail
        )
    }

    if (state.showSkipDialog) {
        var skipReason by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = onDismissSkipDialog,
            title = { Text("Pular Exercício", color = AppTheme.colors.textPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Deseja pular ${currentExercise.name}?", color = AppTheme.colors.textSecondary)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = skipReason,
                        onValueChange = { skipReason = it },
                        label = { Text("Motivo opcional (ex: máquina ocupada, desconforto)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppTheme.colors.primary,
                            unfocusedBorderColor = AppTheme.colors.border,
                            focusedTextColor = AppTheme.colors.textPrimary,
                            unfocusedTextColor = AppTheme.colors.textPrimary,
                            focusedContainerColor = AppTheme.colors.surfaceVariant,
                            unfocusedContainerColor = AppTheme.colors.surfaceVariant
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { onSkipExercise(skipReason) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Pular", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissSkipDialog) {
                    Text("Cancelar", color = AppTheme.colors.textMuted)
                }
            },
            containerColor = AppTheme.colors.surface
        )
    }
}

@Composable
fun SetLogRow(
    setLog: ExerciseSetLog,
    onUpdateValues: (Float, Int, Int) -> Unit,
    onComplete: () -> Unit
) {
    var weightText by remember(setLog.weightKg) { mutableStateOf(setLog.weightKg.let { if (it % 1 == 0f) it.toInt().toString() else it.toString() }) }
    var repsText by remember(setLog.reps) { mutableStateOf(setLog.reps.toString()) }
    var rirVal by remember(setLog.rir) { mutableIntStateOf(setLog.rir) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (setLog.completed) AppTheme.colors.primaryContainer else AppTheme.colors.surface
        ),
        border = BorderStroke(
            1.dp,
            if (setLog.completed) AppTheme.colors.primary.copy(alpha = 0.4f) else AppTheme.colors.border
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Set Number badge
            Surface(
                color = if (setLog.completed) AppTheme.colors.primary else AppTheme.colors.surfaceVariant,
                shape = CircleShape,
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (setLog.completed) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    } else {
                        Text(
                            text = "${setLog.setNumber}",
                            color = AppTheme.colors.textPrimary,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Weight Input
            Column(modifier = Modifier.weight(1.2f)) {
                Text("Carga (kg)", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.textMuted)
                OutlinedTextField(
                    value = weightText,
                    onValueChange = {
                        weightText = it
                        val parsed = it.toFloatOrNull() ?: setLog.weightKg
                        onUpdateValues(parsed, repsText.toIntOrNull() ?: setLog.reps, rirVal)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppTheme.colors.primary,
                        unfocusedBorderColor = AppTheme.colors.border,
                        focusedTextColor = AppTheme.colors.textPrimary,
                        unfocusedTextColor = AppTheme.colors.textPrimary,
                        focusedContainerColor = AppTheme.colors.surfaceVariant,
                        unfocusedContainerColor = AppTheme.colors.surfaceVariant
                    ),
                    modifier = Modifier.height(48.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Reps Input
            Column(modifier = Modifier.weight(1f)) {
                Text("Reps", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.textMuted)
                OutlinedTextField(
                    value = repsText,
                    onValueChange = {
                        repsText = it
                        val parsed = it.toIntOrNull() ?: setLog.reps
                        onUpdateValues(weightText.toFloatOrNull() ?: setLog.weightKg, parsed, rirVal)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppTheme.colors.primary,
                        unfocusedBorderColor = AppTheme.colors.border,
                        focusedTextColor = AppTheme.colors.textPrimary,
                        unfocusedTextColor = AppTheme.colors.textPrimary,
                        focusedContainerColor = AppTheme.colors.surfaceVariant,
                        unfocusedContainerColor = AppTheme.colors.surfaceVariant
                    ),
                    modifier = Modifier.height(48.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Complete Button
            IconButton(
                onClick = onComplete,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (setLog.completed) AppTheme.colors.primary else AppTheme.colors.surfaceVariant)
                    .testTag("complete_set_${setLog.setNumber}")
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Concluir série",
                    tint = if (setLog.completed) Color.White else AppTheme.colors.textMuted
                )
            }
        }
    }
}

@Composable
fun WorkoutCompletionCelebrationScreen(
    template: WorkoutTemplate,
    durationMin: Int,
    completedExercises: Int,
    totalExercises: Int,
    completedSets: Int,
    totalSets: Int,
    onFinalize: () -> Unit,
    onGoToCheckIn: () -> Unit,
    onGoToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .testTag("workout_completion_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🎉", fontSize = 56.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "TREINO CONCLUÍDO!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = AppTheme.colors.textPrimary,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "${template.title} — ${template.subtitle}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Stats Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
            border = BorderStroke(1.dp, AppTheme.colors.border),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Tempo", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.textMuted)
                        Text("$durationMin min", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = AppTheme.colors.textPrimary)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Exercícios", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.textMuted)
                        Text("$completedExercises/$totalExercises", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = AppTheme.colors.primary)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Séries", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.textMuted)
                        Text("$completedSets/$totalSets", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = AppTheme.colors.blueText)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = AppTheme.colors.surfaceVariant,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, AppTheme.colors.border),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🏃‍♂️", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Cardio final: ${template.cardioText}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.colors.textSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Motivational principle
        Text(
            text = "\"Mais um treino concluído. A consistência está construindo seu resultado.\"",
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.colors.primary,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onFinalize,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("finalize_workout_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary)
        ) {
            Text("FINALIZAR", fontWeight = FontWeight.Black, color = Color.White, style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onGoToCheckIn,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Registrar check-in semanal", color = AppTheme.colors.blueText, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onGoToHome) {
            Text("Voltar para início", color = AppTheme.colors.textMuted)
        }
    }
}

