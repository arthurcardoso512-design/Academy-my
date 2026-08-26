package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.entity.BodyMeasurement
import com.example.data.entity.EvolutionPhoto
import com.example.data.entity.ExerciseSetLog
import com.example.data.entity.UserProfile
import com.example.data.entity.WeeklyCheckIn
import com.example.data.entity.WeightRecord
import com.example.data.entity.WorkoutSession
import com.example.data.model.ExerciseTemplate
import com.example.data.model.WorkoutCatalog
import com.example.data.model.WorkoutTemplate
import com.example.data.repository.FitnessRepository
import com.example.ui.theme.AppThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class ActiveWorkoutUiState(
    val activeSession: WorkoutSession? = null,
    val workoutTemplate: WorkoutTemplate? = null,
    val currentExerciseIndex: Int = 0,
    val sessionLogs: List<ExerciseSetLog> = emptyList(),
    val previousExerciseLogs: List<ExerciseSetLog> = emptyList(),
    val isRestTimerVisible: Boolean = false,
    val restTimerSeconds: Int = 90,
    val isExerciseDetailVisible: Boolean = false,
    val isRirExplainerVisible: Boolean = false,
    val showLargeLoadAlert: Boolean = false,
    val isWorkoutFinished: Boolean = false,
    val performanceFeedback: String? = null,
    val showSkipDialog: Boolean = false
)

class FitnessViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FitnessRepository
    private val prefs = application.getSharedPreferences("treino_evolucao_prefs", Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(
        when (prefs.getString("theme_mode", "SYSTEM")) {
            "LIGHT" -> AppThemeMode.LIGHT
            "DARK" -> AppThemeMode.DARK
            else -> AppThemeMode.SYSTEM
        }
    )
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
        prefs.edit().putString("theme_mode", mode.name).apply()
    }

    fun toggleDarkMode() {
        val next = when (_themeMode.value) {
            AppThemeMode.LIGHT -> AppThemeMode.DARK
            AppThemeMode.DARK -> AppThemeMode.SYSTEM
            AppThemeMode.SYSTEM -> AppThemeMode.LIGHT
        }
        setThemeMode(next)
    }

    val userProfile: StateFlow<UserProfile>
    val allSessions: StateFlow<List<WorkoutSession>>
    val completedSessions: StateFlow<List<WorkoutSession>>
    val allWeightRecords: StateFlow<List<WeightRecord>>
    val allMeasurements: StateFlow<List<BodyMeasurement>>
    val allCheckIns: StateFlow<List<WeeklyCheckIn>>
    val allPhotos: StateFlow<List<EvolutionPhoto>>
    val allCompletedLogs: StateFlow<List<ExerciseSetLog>>

    private val _nextWorkout = MutableStateFlow<WorkoutTemplate>(WorkoutCatalog.TREINO_A)
    val nextWorkout: StateFlow<WorkoutTemplate> = _nextWorkout.asStateFlow()

    private val _activeWorkoutState = MutableStateFlow(ActiveWorkoutUiState())
    val activeWorkoutState: StateFlow<ActiveWorkoutUiState> = _activeWorkoutState.asStateFlow()

    private val _newMilestoneAchieved = MutableStateFlow<Float?>(null)
    val newMilestoneAchieved: StateFlow<Float?> = _newMilestoneAchieved.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = FitnessRepository(database.fitnessDao())

        userProfile = repository.userProfile
            .map { it ?: UserProfile() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

        allSessions = repository.allSessions
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        completedSessions = repository.completedSessions
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allWeightRecords = repository.allWeightRecords
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allMeasurements = repository.allMeasurements
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allCheckIns = repository.allCheckIns
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allPhotos = repository.allEvolutionPhotos
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allCompletedLogs = repository.allCompletedLogs
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Automatically observe completed sessions to update next workout template
        viewModelScope.launch {
            repository.completedSessions.collect { sessions ->
                val lastSession = sessions.firstOrNull()
                val nextCode = WorkoutCatalog.getNextWorkoutCode(lastSession?.workoutCode)
                _nextWorkout.value = WorkoutCatalog.getWorkout(nextCode)
            }
        }

        // Observe active session from DB if any exists (e.g. app reopened)
        viewModelScope.launch {
            repository.activeSession.collect { active ->
                if (active != null) {
                    val template = WorkoutCatalog.getWorkout(active.workoutCode)
                    val logs = repository.getLogsForSessionOnce(active.id)
                    val firstIncompleteIndex = getFirstIncompleteExerciseIndex(template, logs)

                    _activeWorkoutState.value = _activeWorkoutState.value.copy(
                        activeSession = active,
                        workoutTemplate = template,
                        currentExerciseIndex = firstIncompleteIndex,
                        sessionLogs = logs
                    )
                    loadPreviousLogsForCurrentExercise(active.id, template, firstIncompleteIndex)
                }
            }
        }
    }

    private fun getFirstIncompleteExerciseIndex(template: WorkoutTemplate, logs: List<ExerciseSetLog>): Int {
        for (i in template.exercises.indices) {
            val exName = template.exercises[i].name
            val exLogs = logs.filter { it.exerciseName == exName }
            if (exLogs.any { !it.completed && !it.skipped }) {
                return i
            }
        }
        return (template.exercises.size - 1).coerceAtLeast(0)
    }

    fun startWorkout(workoutCode: String) {
        viewModelScope.launch {
            val sessionId = repository.startWorkoutSession(workoutCode)
            val template = WorkoutCatalog.getWorkout(workoutCode)
            val session = repository.getSessionById(sessionId)
            val logs = repository.getLogsForSessionOnce(sessionId)

            _activeWorkoutState.value = ActiveWorkoutUiState(
                activeSession = session,
                workoutTemplate = template,
                currentExerciseIndex = 0,
                sessionLogs = logs,
                isWorkoutFinished = false
            )
            loadPreviousLogsForCurrentExercise(sessionId, template, 0)
        }
    }

    private suspend fun loadPreviousLogsForCurrentExercise(
        sessionId: Long,
        template: WorkoutTemplate,
        exerciseIndex: Int
    ) {
        if (exerciseIndex in template.exercises.indices) {
            val ex = template.exercises[exerciseIndex]
            val prev = repository.getPreviousLogsForExercise(ex.name, sessionId)
            _activeWorkoutState.value = _activeWorkoutState.value.copy(
                previousExerciseLogs = prev
            )
        }
    }

    fun updateSetLogValues(logId: Long, weightKg: Float, reps: Int, rir: Int) {
        viewModelScope.launch {
            val currentLogs = _activeWorkoutState.value.sessionLogs.toMutableList()
            val index = currentLogs.indexOfFirst { it.id == logId }
            if (index >= 0) {
                val updated = currentLogs[index].copy(weightKg = weightKg, reps = reps, rir = rir)
                currentLogs[index] = updated
                repository.updateSetLog(updated)
                _activeWorkoutState.value = _activeWorkoutState.value.copy(sessionLogs = currentLogs)
            }
        }
    }

    fun markSetComplete(logId: Long) {
        viewModelScope.launch {
            val state = _activeWorkoutState.value
            val currentLogs = state.sessionLogs.toMutableList()
            val index = currentLogs.indexOfFirst { it.id == logId }
            if (index >= 0) {
                val setLog = currentLogs[index]
                val updated = setLog.copy(completed = true)
                currentLogs[index] = updated
                repository.updateSetLog(updated)

                val template = state.workoutTemplate ?: return@launch
                val currentEx = template.exercises.getOrNull(state.currentExerciseIndex)

                // Check load jump alert (e.g. >20% increase from previous load)
                val prevSet = state.previousExerciseLogs.find { it.setNumber == updated.setNumber }
                val prevWeight = prevSet?.weightKg ?: 0f
                val showLoadAlert = prevWeight > 0 && (updated.weightKg - prevWeight) >= 15f

                // Check performance feedback
                val currentExLogs = currentLogs.filter { it.exerciseName == updated.exerciseName }
                val allSetsCompleted = currentExLogs.all { it.completed || it.skipped }
                var feedback: String? = null

                if (allSetsCompleted && currentEx != null) {
                    val allRepsAtMax = currentExLogs.all { it.reps >= currentEx.maxReps }
                    if (allRepsAtMax) {
                        feedback = "Boa! Você atingiu o topo da faixa de repetições (${currentEx.maxReps}). Na próxima sessão, avalie aumentar um pouco a carga mantendo boa técnica."
                    } else if (prevWeight > 0 && updated.weightKg >= prevWeight) {
                        val totalRepsNow = currentExLogs.sumOf { it.reps }
                        val prevLogsEx = state.previousExerciseLogs
                        val totalRepsPrev = prevLogsEx.sumOf { it.reps }
                        if (totalRepsNow > totalRepsPrev || updated.weightKg > prevWeight) {
                            feedback = "Você melhorou sua performance! 📈"
                        }
                    }
                }

                val restTime = currentEx?.restSecondsMin ?: 90

                _activeWorkoutState.value = state.copy(
                    sessionLogs = currentLogs,
                    isRestTimerVisible = !allSetsCompleted,
                    restTimerSeconds = restTime,
                    showLargeLoadAlert = showLoadAlert,
                    performanceFeedback = feedback
                )
            }
        }
    }

    fun dismissRestTimer() {
        _activeWorkoutState.value = _activeWorkoutState.value.copy(isRestTimerVisible = false)
    }

    fun goToNextExercise() {
        viewModelScope.launch {
            val state = _activeWorkoutState.value
            val template = state.workoutTemplate ?: return@launch
            val nextIndex = state.currentExerciseIndex + 1

            if (nextIndex < template.exercises.size) {
                _activeWorkoutState.value = state.copy(
                    currentExerciseIndex = nextIndex,
                    performanceFeedback = null,
                    showLargeLoadAlert = false,
                    isRestTimerVisible = false
                )
                state.activeSession?.let {
                    loadPreviousLogsForCurrentExercise(it.id, template, nextIndex)
                }
            } else {
                // Workout completed!
                _activeWorkoutState.value = state.copy(
                    isWorkoutFinished = true,
                    performanceFeedback = null
                )
            }
        }
    }

    fun jumpToExercise(index: Int) {
        viewModelScope.launch {
            val state = _activeWorkoutState.value
            val template = state.workoutTemplate ?: return@launch
            if (index in template.exercises.indices) {
                _activeWorkoutState.value = state.copy(
                    currentExerciseIndex = index,
                    performanceFeedback = null,
                    showLargeLoadAlert = false,
                    isRestTimerVisible = false
                )
                state.activeSession?.let {
                    loadPreviousLogsForCurrentExercise(it.id, template, index)
                }
            }
        }
    }

    fun skipCurrentExercise(reason: String) {
        viewModelScope.launch {
            val state = _activeWorkoutState.value
            val template = state.workoutTemplate ?: return@launch
            val currentEx = template.exercises.getOrNull(state.currentExerciseIndex) ?: return@launch

            val updatedLogs = state.sessionLogs.map { log ->
                if (log.exerciseName == currentEx.name) {
                    val skippedLog = log.copy(skipped = true, completed = false, skipReason = reason)
                    repository.updateSetLog(skippedLog)
                    skippedLog
                } else log
            }

            _activeWorkoutState.value = state.copy(
                sessionLogs = updatedLogs,
                showSkipDialog = false
            )
            goToNextExercise()
        }
    }

    fun finishWorkout(cardioMinutes: Int = 8, notes: String = "") {
        viewModelScope.launch {
            val sessionId = _activeWorkoutState.value.activeSession?.id ?: return@launch
            repository.completeWorkoutSession(sessionId, cardioMinutes, notes)
            _activeWorkoutState.value = ActiveWorkoutUiState(isWorkoutFinished = false)
        }
    }

    fun cancelWorkout() {
        viewModelScope.launch {
            val sessionId = _activeWorkoutState.value.activeSession?.id ?: return@launch
            repository.cancelWorkoutSession(sessionId)
            _activeWorkoutState.value = ActiveWorkoutUiState()
        }
    }

    fun setRirExplainerVisible(visible: Boolean) {
        _activeWorkoutState.value = _activeWorkoutState.value.copy(isRirExplainerVisible = visible)
    }

    fun setExerciseDetailVisible(visible: Boolean) {
        _activeWorkoutState.value = _activeWorkoutState.value.copy(isExerciseDetailVisible = visible)
    }

    fun setSkipDialogVisible(visible: Boolean) {
        _activeWorkoutState.value = _activeWorkoutState.value.copy(showSkipDialog = visible)
    }

    // Weight & Measurement Actions
    fun recordWeight(weightKg: Float, notes: String) {
        viewModelScope.launch {
            repository.addWeightRecord(weightKg, notes)
            checkMilestone(weightKg)
        }
    }

    private fun checkMilestone(weightKg: Float) {
        val milestones = listOf(120f, 115f, 110f, 105f, 100f)
        for (m in milestones) {
            if (weightKg <= m) {
                // Trigger milestone celebration if passed
                _newMilestoneAchieved.value = m
                break
            }
        }
    }

    fun dismissMilestone() {
        _newMilestoneAchieved.value = null
    }

    fun recordMeasurements(waist: Float, abdomen: Float, chest: Float, arm: Float, thigh: Float, notes: String) {
        viewModelScope.launch {
            repository.addBodyMeasurement(waist, abdomen, chest, arm, thigh, notes)
        }
    }

    fun recordWeeklyCheckIn(
        avgWeightKg: Float,
        avgSteps: Int,
        workoutsCompleted: Int,
        energy: Int,
        sleep: Int,
        hunger: Int,
        hasPain: Boolean,
        painLocation: String,
        notes: String
    ) {
        viewModelScope.launch {
            val weekNum = (allCheckIns.value.size + 1)
            val feedback = buildString {
                append("Esta semana você realizou $workoutsCompleted de 3 treinos planejados. ")
                if (avgWeightKg <= 122f) {
                    append("Seu peso apresentou tendência de queda consistente. ")
                } else {
                    append("Mantenha o foco nos hábitos diários. ")
                }
                append("Você manteve sua consistência e disciplina na musculação!")
            }

            val checkIn = WeeklyCheckIn(
                dateMillis = System.currentTimeMillis(),
                weekNumber = weekNum,
                avgWeightKg = avgWeightKg,
                avgSteps = avgSteps,
                workoutsCompleted = workoutsCompleted,
                energyScore = energy,
                sleepScore = sleep,
                hungerScore = hunger,
                hasPain = hasPain,
                painLocation = painLocation,
                notes = notes,
                summaryFeedback = feedback
            )
            repository.addWeeklyCheckIn(checkIn)
        }
    }

    fun recordEvolutionPhoto(category: String, front: String, side: String, back: String, weight: Float) {
        viewModelScope.launch {
            val photo = EvolutionPhoto(
                dateMillis = System.currentTimeMillis(),
                category = category,
                frontTag = front,
                sideTag = side,
                backTag = back,
                weightAtTime = weight
            )
            repository.addEvolutionPhoto(photo)
        }
    }

    fun toggleTreinoD(unlocked: Boolean) {
        viewModelScope.launch {
            val current = repository.getUserProfileOnce()
            repository.saveUserProfile(current.copy(unlockedTreinoD = unlocked))
        }
    }

    fun updateProfile(name: String, targetWeight: Float) {
        viewModelScope.launch {
            val current = repository.getUserProfileOnce()
            repository.saveUserProfile(current.copy(name = name, targetWeightKg = targetWeight))
        }
    }
}
