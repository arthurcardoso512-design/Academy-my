package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ExerciseTemplate
import kotlinx.coroutines.delay

@Composable
fun RestTimerDialog(
    initialSeconds: Int,
    onDismiss: () -> Unit,
    onFinished: () -> Unit
) {
    var totalSeconds by remember { mutableIntStateOf(initialSeconds) }
    var secondsRemaining by remember { mutableIntStateOf(initialSeconds) }

    LaunchedEffect(secondsRemaining) {
        if (secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining--
        } else {
            delay(500L)
            onFinished()
        }
    }

    val progress = if (totalSeconds > 0) secondsRemaining.toFloat() / totalSeconds.toFloat() else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "RestProgress")

    val minutes = secondsRemaining / 60
    val secs = secondsRemaining % 60
    val timeFormatted = String.format("%02d:%02d", minutes, secs)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("rest_timer_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Hora de descansar",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "😮‍💨", fontSize = 24.sp)
                }

                Text(
                    text = "Recupere o fôlego antes da próxima série",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                // Circular Countdown Display
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(160.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFF334155),
                        strokeWidth = 10.dp,
                    )
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = if (secondsRemaining <= 10) Color(0xFF10B981) else Color(0xFF06B6D4),
                        strokeWidth = 10.dp,
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = timeFormatted,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 36.sp
                        )
                        if (secondsRemaining == 0) {
                            Text(
                                text = "Pronto! 💪",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF10B981),
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = "Descanso",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Adjustment Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            totalSeconds += 15
                            secondsRemaining += 15
                        },
                        modifier = Modifier.weight(1f).testTag("add_15s_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("+15s", color = Color(0xFF38BDF8), fontWeight = FontWeight.SemiBold)
                    }

                    OutlinedButton(
                        onClick = {
                            totalSeconds += 30
                            secondsRemaining += 30
                        },
                        modifier = Modifier.weight(1f).testTag("add_30s_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("+30s", color = Color(0xFF38BDF8), fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (secondsRemaining == 0) onFinished() else onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("skip_rest_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (secondsRemaining == 0) Color(0xFF10B981) else Color(0xFF334155)
                    )
                ) {
                    Text(
                        text = if (secondsRemaining == 0) "Próxima Série 💪" else "Pular descanso",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun RirExplainerDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("rir_explainer_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "O que é RIR?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "RIR significa Repetições em Reserva (Reps in Reserve). Indica quantas repetições você ainda aguentaria fazer antes de falhar o movimento.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFCBD5E1),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // RIR Visual Scale
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E293B))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RirScaleRow("RIR 3", "Conseguiria fazer mais 3 repetições", Color(0xFF10B981), "Ideal para retorno e base")
                    RirScaleRow("RIR 2", "Conseguiria fazer mais 2 repetições", Color(0xFF38BDF8), "Ótimo estímulo hipertrófico")
                    RirScaleRow("RIR 1", "Conseguiria fazer apenas mais 1", Color(0xFFF59E0B), "Esforço muito intenso")
                    RirScaleRow("RIR 0", "Falha concêntrica (não sobe mais)", Color(0xFFEF4444), "Evite nesta fase")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Guidance Box
                Surface(
                    color = Color(0xFF064E3B).copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "💡", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Durante as primeiras semanas, foque na técnica e controle. Não precisa treinar até a falha.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA7F3D0),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Entendi", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun RirScaleRow(rirLabel: String, meaning: String, color: Color, recommendation: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = 46.dp, height = 24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = rirLabel, color = color, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = meaning, color = Color.White, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
            Text(text = recommendation, color = Color(0xFF94A3B8), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun ExerciseDetailDialog(
    exercise: ExerciseTemplate,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("exercise_detail_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // High-resolution machine illustration
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1E293B)),
                    contentAlignment = Alignment.Center
                ) {
                    ExerciseIllustration(
                        exerciseName = exercise.name,
                        size = 120.dp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Equipment & Muscles tags
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Equipamento", color = Color(0xFF64748B), style = MaterialTheme.typography.labelSmall)
                            Text(exercise.equipment, color = Color.White, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Surface(
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Músculos Foco", color = Color(0xFF64748B), style = MaterialTheme.typography.labelSmall)
                            Text(exercise.muscleFocus, color = Color(0xFF34D399), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Program details row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Séries: ${exercise.defaultSets} × ${exercise.minReps}–${exercise.maxReps}", color = Color(0xFFCBD5E1), style = MaterialTheme.typography.bodyMedium)
                    Text("Descanso: ${exercise.restSecondsMin}–${exercise.restSecondsMax}s", color = Color(0xFF38BDF8), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Instruções de Execução:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = exercise.executionTips,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF94A3B8),
                    lineHeight = 20.sp
                )

                if (exercise.alertWarning != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0xFF78350F).copy(alpha = 0.5f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(exercise.alertWarning, color = Color(0xFFFDE68A), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                if (exercise.alternativeExercise != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Alternativa: ${exercise.alternativeExercise}",
                        color = Color(0xFF38BDF8),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Fechar", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun MilestoneCelebrationDialog(
    milestoneWeightKg: Float,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("milestone_celebration_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0xFFF59E0B), Color(0xFFD97706))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Meta alcançada! 🔥",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Você atingiu a marca de ${milestoneWeightKg.toInt()} kg! Sua consistência nos treinos está gerando resultados reais e duradouros.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFCBD5E1),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Continuar focado! 💪", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
