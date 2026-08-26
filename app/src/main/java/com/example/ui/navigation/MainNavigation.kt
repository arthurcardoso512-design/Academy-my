package com.example.ui.navigation

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.HabitsSafetyScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MeasurementsScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.WeeklyCheckInScreen
import com.example.ui.screens.WorkoutActiveScreen
import com.example.ui.screens.WorkoutsCatalogScreen
import com.example.ui.theme.AppTheme
import com.example.ui.viewmodel.FitnessViewModel

enum class Screen(val title: String, val icon: ImageVector) {
    HOME("Início", Icons.Default.Home),
    WORKOUTS("Treinos", Icons.Default.FitnessCenter),
    ACTIVE_WORKOUT("Em Treino", Icons.Default.PlayArrow),
    PROGRESS("Evolução", Icons.Default.TrendingDown),
    MEASUREMENTS("Medidas", Icons.Default.Straighten),
    CHECK_IN("Check-in", Icons.Default.CheckCircle),
    HABITS("Hábitos", Icons.Default.HealthAndSafety),
    HISTORY("Histórico", Icons.Default.History)
}

@Composable
fun MainApp(viewModel: FitnessViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }

    val userProfile by viewModel.userProfile.collectAsState()
    val nextWorkout by viewModel.nextWorkout.collectAsState()
    val recentSessions by viewModel.allSessions.collectAsState()
    val completedSessions by viewModel.completedSessions.collectAsState()
    val weightRecords by viewModel.allWeightRecords.collectAsState()
    val measurements by viewModel.allMeasurements.collectAsState()
    val checkIns by viewModel.allCheckIns.collectAsState()
    val photos by viewModel.allPhotos.collectAsState()
    val completedLogs by viewModel.allCompletedLogs.collectAsState()
    val activeState by viewModel.activeWorkoutState.collectAsState()
    val newMilestone by viewModel.newMilestoneAchieved.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()

    val isWorkoutActive = activeState.activeSession != null

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppTheme.colors.background,
        bottomBar = {
            // Show bottom navigation bar when not in active workout screen (or keep persistent bar)
            if (currentScreen != Screen.ACTIVE_WORKOUT) {
                Column {
                    // Floating Mini Active Bar if a workout is in progress
                    if (isWorkoutActive) {
                        Surface(
                            color = AppTheme.colors.heroCardBg,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { currentScreen = Screen.ACTIVE_WORKOUT }
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.colors.border)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🏋️", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Em andamento: ${activeState.workoutTemplate?.title}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = AppTheme.colors.heroCardText
                                    )
                                }
                                Surface(
                                    color = AppTheme.colors.primary,
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "Continuar →",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Main Bottom Navigation Bar
                    Surface(
                        color = AppTheme.colors.navBarBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.colors.border)
                    ) {
                        NavigationBar(
                            containerColor = AppTheme.colors.navBarBg,
                            tonalElevation = 0.dp,
                            modifier = Modifier.testTag("bottom_nav_bar")
                        ) {
                            val navItems = listOf(
                                Screen.HOME,
                                Screen.WORKOUTS,
                                Screen.PROGRESS,
                                Screen.MEASUREMENTS,
                                Screen.HABITS
                            )

                            navItems.forEach { screen ->
                                NavigationBarItem(
                                    selected = currentScreen == screen,
                                    onClick = { currentScreen = screen },
                                    icon = {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = screen.title
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = screen.title,
                                            fontWeight = if (currentScreen == screen) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 11.sp
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = AppTheme.colors.onPrimaryContainer,
                                        selectedTextColor = AppTheme.colors.onPrimaryContainer,
                                        indicatorColor = AppTheme.colors.primaryContainer,
                                        unselectedIconColor = AppTheme.colors.textMuted,
                                        unselectedTextColor = AppTheme.colors.textMuted
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.HOME -> HomeScreen(
                    userProfile = userProfile,
                    nextWorkout = nextWorkout,
                    recentSessions = recentSessions,
                    weightRecords = weightRecords,
                    newMilestone = newMilestone,
                    currentThemeMode = themeMode,
                    onSelectTheme = { viewModel.setThemeMode(it) },
                    onToggleTheme = { viewModel.toggleDarkMode() },
                    onStartWorkout = { code ->
                        viewModel.startWorkout(code)
                        currentScreen = Screen.ACTIVE_WORKOUT
                    },
                    onNavigateToWorkouts = { currentScreen = Screen.WORKOUTS },
                    onNavigateToProgress = { currentScreen = Screen.PROGRESS },
                    onNavigateToMeasurements = { currentScreen = Screen.MEASUREMENTS },
                    onNavigateToHistory = { currentScreen = Screen.HISTORY },
                    onNavigateToCheckIn = { currentScreen = Screen.CHECK_IN },
                    onNavigateToHabits = { currentScreen = Screen.HABITS },
                    onRecordWeightClick = { currentScreen = Screen.PROGRESS },
                    onDismissMilestone = { viewModel.dismissMilestone() }
                )

                Screen.WORKOUTS -> WorkoutsCatalogScreen(
                    userProfile = userProfile,
                    onStartWorkout = { code ->
                        viewModel.startWorkout(code)
                        currentScreen = Screen.ACTIVE_WORKOUT
                    },
                    onToggleTreinoD = { unlocked ->
                        viewModel.toggleTreinoD(unlocked)
                    }
                )

                Screen.ACTIVE_WORKOUT -> WorkoutActiveScreen(
                    state = activeState,
                    onUpdateSetLog = { id, wt, reps, rir -> viewModel.updateSetLogValues(id, wt, reps, rir) },
                    onCompleteSet = { id -> viewModel.markSetComplete(id) },
                    onNextExercise = { viewModel.goToNextExercise() },
                    onJumpToExercise = { index -> viewModel.jumpToExercise(index) },
                    onSkipExercise = { reason -> viewModel.skipCurrentExercise(reason) },
                    onFinishWorkout = { cardio, notes -> viewModel.finishWorkout(cardio, notes) },
                    onCancelWorkout = {
                        viewModel.cancelWorkout()
                        currentScreen = Screen.HOME
                    },
                    onDismissRestTimer = { viewModel.dismissRestTimer() },
                    onOpenRirExplainer = { viewModel.setRirExplainerVisible(true) },
                    onDismissRirExplainer = { viewModel.setRirExplainerVisible(false) },
                    onOpenExerciseDetail = { viewModel.setExerciseDetailVisible(true) },
                    onDismissExerciseDetail = { viewModel.setExerciseDetailVisible(false) },
                    onOpenSkipDialog = { viewModel.setSkipDialogVisible(true) },
                    onDismissSkipDialog = { viewModel.setSkipDialogVisible(false) },
                    onNavigateToCheckIn = { currentScreen = Screen.CHECK_IN },
                    onNavigateToHome = { currentScreen = Screen.HOME }
                )

                Screen.PROGRESS -> ProgressScreen(
                    userProfile = userProfile,
                    weightRecords = weightRecords,
                    measurements = measurements,
                    completedLogs = completedLogs,
                    completedSessions = completedSessions,
                    checkIns = checkIns,
                    onAddWeight = { wt, notes -> viewModel.recordWeight(wt, notes) }
                )

                Screen.MEASUREMENTS -> MeasurementsScreen(
                    measurements = measurements,
                    photos = photos,
                    onAddMeasurement = { waist, ab, ch, arm, thigh, notes ->
                        viewModel.recordMeasurements(waist, ab, ch, arm, thigh, notes)
                    },
                    onAddPhotoRecord = { cat, f, s, b, wt ->
                        viewModel.recordEvolutionPhoto(cat, f, s, b, wt)
                    }
                )

                Screen.CHECK_IN -> WeeklyCheckInScreen(
                    userProfile = userProfile,
                    checkIns = checkIns,
                    onSubmitCheckIn = { wt, steps, workouts, energy, sleep, hunger, pain, location, notes ->
                        viewModel.recordWeeklyCheckIn(wt, steps, workouts, energy, sleep, hunger, pain, location, notes)
                    }
                )

                Screen.HABITS -> HabitsSafetyScreen(
                    userProfile = userProfile,
                    currentThemeMode = themeMode,
                    onSelectTheme = { viewModel.setThemeMode(it) }
                )

                Screen.HISTORY -> HistoryScreen(sessions = recentSessions)
            }
        }
    }
}

