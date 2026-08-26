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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.BodyMeasurement
import com.example.data.entity.EvolutionPhoto
import com.example.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MeasurementsScreen(
    measurements: List<BodyMeasurement>,
    photos: List<EvolutionPhoto>,
    onAddMeasurement: (Float, Float, Float, Float, Float, String) -> Unit,
    onAddPhotoRecord: (String, String, String, String, Float) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Medidas Corporais, 1: Fotos de Evolução
    var showAddMeasurementModal by remember { mutableStateOf(false) }
    var showAddPhotoModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .padding(bottom = 80.dp)
            .testTag("measurements_screen")
    ) {
        Text(
            text = "Medidas & Composição",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = AppTheme.colors.textPrimary
        )
        Text(
            text = "Acompanhe circunferências e registro fotográfico quinzenal",
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.colors.textMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Selector
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = AppTheme.colors.surface,
            contentColor = AppTheme.colors.primary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = AppTheme.colors.primary
                )
            },
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(AppTheme.colors.surface)
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("📏 Medidas em cm", fontWeight = FontWeight.Bold, color = if (selectedTab == 0) AppTheme.colors.primary else AppTheme.colors.textMuted) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("📸 Fotos de Evolução", fontWeight = FontWeight.Bold, color = if (selectedTab == 1) AppTheme.colors.primary else AppTheme.colors.textMuted) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (selectedTab == 0) {
            // MEASUREMENTS TAB
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Circunferências Registradas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.textPrimary
                )

                Button(
                    onClick = { showAddMeasurementModal = true },
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("add_measurement_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Nova Medição", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            measurements.forEach { m ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
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
                            Text(
                                text = SimpleDateFormat("dd 'de' MMMM, yyyy", Locale("pt", "BR")).format(Date(m.dateMillis)),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.textPrimary
                            )
                            if (m.notes.isNotEmpty()) {
                                Text(m.notes, style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.blueText, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MeasurementItem("Cintura", "${m.waistCm} cm", AppTheme.colors.amberText)
                            MeasurementItem("Abdômen", "${m.abdomenCm} cm", AppTheme.colors.amberText)
                            MeasurementItem("Tórax", "${m.chestCm} cm", AppTheme.colors.blueText)
                            MeasurementItem("Braço", "${m.armCm} cm", AppTheme.colors.primary)
                            MeasurementItem("Coxa", "${m.thighCm} cm", AppTheme.colors.primary)
                        }
                    }
                }
            }
        } else {
            // EVOLUTION PHOTOS TAB
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Registro Fotográfico",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.textPrimary
                )

                Button(
                    onClick = { showAddPhotoModal = true },
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("add_photo_button")
                ) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Adicionar Fotos", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = AppTheme.colors.primaryContainer,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("💡", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Tire fotos no mesmo local, horário e iluminação (em jejum, a cada 15 a 30 dias) para uma comparação precisa de composição.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.colors.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sample / Added Photo Sets
            val displayPhotos = photos.ifEmpty {
                listOf(
                    EvolutionPhoto(
                        id = 1,
                        dateMillis = System.currentTimeMillis() - 7 * 86400000L,
                        category = "Início",
                        frontTag = "Frente (122.0 kg)",
                        sideTag = "Lado (122.0 kg)",
                        backTag = "Costas (122.0 kg)",
                        weightAtTime = 122.0f
                    ),
                    EvolutionPhoto(
                        id = 2,
                        dateMillis = System.currentTimeMillis(),
                        category = "Semana 2",
                        frontTag = "Frente (121.2 kg)",
                        sideTag = "Lado (121.2 kg)",
                        backTag = "Costas (121.2 kg)",
                        weightAtTime = 121.2f
                    )
                )
            }

            displayPhotos.forEach { photoSet ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
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
                            Text(
                                text = "${photoSet.category} — ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(photoSet.dateMillis))}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.textPrimary
                            )
                            Surface(
                                color = AppTheme.colors.primaryContainer,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = "${photoSet.weightAtTime} kg",
                                    color = AppTheme.colors.onPrimaryContainer,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            PhotoAngleCard("Frente", Modifier.weight(1f))
                            PhotoAngleCard("Lado", Modifier.weight(1f))
                            PhotoAngleCard("Costas", Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }

    // ADD MEASUREMENT MODAL
    if (showAddMeasurementModal) {
        var waist by remember { mutableStateOf("") }
        var abdomen by remember { mutableStateOf("") }
        var chest by remember { mutableStateOf("") }
        var arm by remember { mutableStateOf("") }
        var thigh by remember { mutableStateOf("") }
        var notes by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddMeasurementModal = false },
            title = { Text("Registrar Medidas (cm)", color = AppTheme.colors.textPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(
                        value = waist,
                        onValueChange = { waist = it },
                        label = { Text("Cintura (menor curvatura)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppTheme.colors.primary,
                            unfocusedBorderColor = AppTheme.colors.border,
                            focusedTextColor = AppTheme.colors.textPrimary,
                            unfocusedTextColor = AppTheme.colors.textPrimary,
                            focusedContainerColor = AppTheme.colors.surfaceVariant,
                            unfocusedContainerColor = AppTheme.colors.surfaceVariant
                        )
                    )
                    OutlinedTextField(
                        value = abdomen,
                        onValueChange = { abdomen = it },
                        label = { Text("Abdômen (na altura do umbigo)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppTheme.colors.primary,
                            unfocusedBorderColor = AppTheme.colors.border,
                            focusedTextColor = AppTheme.colors.textPrimary,
                            unfocusedTextColor = AppTheme.colors.textPrimary,
                            focusedContainerColor = AppTheme.colors.surfaceVariant,
                            unfocusedContainerColor = AppTheme.colors.surfaceVariant
                        )
                    )
                    OutlinedTextField(
                        value = chest,
                        onValueChange = { chest = it },
                        label = { Text("Tórax / Peito") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppTheme.colors.primary,
                            unfocusedBorderColor = AppTheme.colors.border,
                            focusedTextColor = AppTheme.colors.textPrimary,
                            unfocusedTextColor = AppTheme.colors.textPrimary,
                            focusedContainerColor = AppTheme.colors.surfaceVariant,
                            unfocusedContainerColor = AppTheme.colors.surfaceVariant
                        )
                    )
                    OutlinedTextField(
                        value = arm,
                        onValueChange = { arm = it },
                        label = { Text("Braço contraído") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppTheme.colors.primary,
                            unfocusedBorderColor = AppTheme.colors.border,
                            focusedTextColor = AppTheme.colors.textPrimary,
                            unfocusedTextColor = AppTheme.colors.textPrimary,
                            focusedContainerColor = AppTheme.colors.surfaceVariant,
                            unfocusedContainerColor = AppTheme.colors.surfaceVariant
                        )
                    )
                    OutlinedTextField(
                        value = thigh,
                        onValueChange = { thigh = it },
                        label = { Text("Coxa medial") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppTheme.colors.primary,
                            unfocusedBorderColor = AppTheme.colors.border,
                            focusedTextColor = AppTheme.colors.textPrimary,
                            unfocusedTextColor = AppTheme.colors.textPrimary,
                            focusedContainerColor = AppTheme.colors.surfaceVariant,
                            unfocusedContainerColor = AppTheme.colors.surfaceVariant
                        )
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Observação (opcional)") },
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
                    onClick = {
                        val w = waist.replace(",", ".").toFloatOrNull() ?: 0f
                        val ab = abdomen.replace(",", ".").toFloatOrNull() ?: 0f
                        val ch = chest.replace(",", ".").toFloatOrNull() ?: 0f
                        val ar = arm.replace(",", ".").toFloatOrNull() ?: 0f
                        val th = thigh.replace(",", ".").toFloatOrNull() ?: 0f
                        if (w > 0f) {
                            onAddMeasurement(w, ab, ch, ar, th, notes)
                            showAddMeasurementModal = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary)
                ) {
                    Text("Salvar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMeasurementModal = false }) { Text("Cancelar", color = AppTheme.colors.textMuted) }
            },
            containerColor = AppTheme.colors.surface
        )
    }

    // ADD PHOTO MODAL
    if (showAddPhotoModal) {
        var category by remember { mutableStateOf("Quinzenal") }
        var weightInput by remember { mutableStateOf("121.2") }

        AlertDialog(
            onDismissRequest = { showAddPhotoModal = false },
            title = { Text("Registrar Registro Fotográfico", color = AppTheme.colors.textPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Salve o registro com fotos nos ângulos padrão:", color = AppTheme.colors.textMuted, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Identificador (ex: Semana 4, Mês 1)") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppTheme.colors.primary,
                            unfocusedBorderColor = AppTheme.colors.border,
                            focusedTextColor = AppTheme.colors.textPrimary,
                            unfocusedTextColor = AppTheme.colors.textPrimary,
                            focusedContainerColor = AppTheme.colors.surfaceVariant,
                            unfocusedContainerColor = AppTheme.colors.surfaceVariant
                        )
                    )
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { weightInput = it },
                        label = { Text("Peso atual (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                    onClick = {
                        val wt = weightInput.replace(",", ".").toFloatOrNull() ?: 121f
                        onAddPhotoRecord(category, "Frente", "Lado", "Costas", wt)
                        showAddPhotoModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary)
                ) {
                    Text("Salvar Registro", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPhotoModal = false }) { Text("Cancelar", color = AppTheme.colors.textMuted) }
            },
            containerColor = AppTheme.colors.surface
        )
    }
}

@Composable
private fun MeasurementItem(label: String, value: String, accent: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.textMuted)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = accent)
    }
}

@Composable
private fun PhotoAngleCard(label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(110.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(AppTheme.colors.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null,
                tint = AppTheme.colors.textMuted,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.textPrimary, fontWeight = FontWeight.SemiBold)
        }
    }
}

