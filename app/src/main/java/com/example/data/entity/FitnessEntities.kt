package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Atleta",
    val initialWeightKg: Float = 122.0f,
    val currentWeightKg: Float = 122.0f,
    val targetWeightKg: Float = 100.0f,
    val initialWaistCm: Float = 120.0f,
    val currentWaistCm: Float = 120.0f,
    val startDateMillis: Long = System.currentTimeMillis(),
    val unlockedTreinoD: Boolean = false
)

@Entity(tableName = "workout_sessions")
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutCode: String, // "A", "B", "C", "D"
    val title: String,
    val dateMillis: Long,
    val startTimeMillis: Long,
    val endTimeMillis: Long = 0,
    val durationMinutes: Int = 0,
    val status: String, // "EM_ANDAMENTO", "CONCLUIDO", "CANCELADO"
    val completedExercisesCount: Int = 0,
    val totalExercisesCount: Int = 7,
    val completedSetsCount: Int = 0,
    val cardioMinutes: Int = 0,
    val notes: String = ""
)

@Entity(tableName = "exercise_set_logs")
data class ExerciseSetLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: Long,
    val workoutCode: String,
    val exerciseName: String,
    val setNumber: Int,
    val weightKg: Float,
    val reps: Int,
    val rir: Int = 2,
    val completed: Boolean = false,
    val skipped: Boolean = false,
    val skipReason: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "weight_records")
data class WeightRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateMillis: Long,
    val weightKg: Float,
    val notes: String = ""
)

@Entity(tableName = "body_measurements")
data class BodyMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateMillis: Long,
    val waistCm: Float, // Cintura
    val abdomenCm: Float, // Abdômen
    val chestCm: Float, // Peito
    val armCm: Float, // Braço
    val thighCm: Float, // Coxa
    val notes: String = ""
)

@Entity(tableName = "weekly_checkins")
data class WeeklyCheckIn(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateMillis: Long,
    val weekNumber: Int,
    val avgWeightKg: Float,
    val avgSteps: Int,
    val workoutsCompleted: Int,
    val energyScore: Int, // 0-10
    val sleepScore: Int,  // 0-10
    val hungerScore: Int, // 0-10
    val hasPain: Boolean,
    val painLocation: String = "",
    val notes: String = "",
    val summaryFeedback: String = ""
)

@Entity(tableName = "evolution_photos")
data class EvolutionPhoto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateMillis: Long,
    val category: String, // "Dia 0", "1 mês", "3 meses", "6 meses", "Livre"
    val frontTag: String = "Frente",
    val sideTag: String = "Lateral",
    val backTag: String = "Costas",
    val weightAtTime: Float = 0f,
    val notes: String = ""
)
