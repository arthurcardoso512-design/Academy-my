package com.example.data.repository

import com.example.data.dao.FitnessDao
import com.example.data.entity.BodyMeasurement
import com.example.data.entity.EvolutionPhoto
import com.example.data.entity.ExerciseSetLog
import com.example.data.entity.UserProfile
import com.example.data.entity.WeeklyCheckIn
import com.example.data.entity.WeightRecord
import com.example.data.entity.WorkoutSession
import com.example.data.model.WorkoutCatalog
import com.example.data.model.WorkoutTemplate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FitnessRepository(private val dao: FitnessDao) {

    val userProfile: Flow<UserProfile?> = dao.getUserProfile()
    val allSessions: Flow<List<WorkoutSession>> = dao.getAllSessions()
    val completedSessions: Flow<List<WorkoutSession>> = dao.getCompletedSessions()
    val activeSession: Flow<WorkoutSession?> = dao.getActiveSession()
    val allWeightRecords: Flow<List<WeightRecord>> = dao.getAllWeightRecords()
    val allMeasurements: Flow<List<BodyMeasurement>> = dao.getAllMeasurements()
    val latestMeasurement: Flow<BodyMeasurement?> = dao.getLatestMeasurement()
    val allCheckIns: Flow<List<WeeklyCheckIn>> = dao.getAllCheckIns()
    val latestCheckIn: Flow<WeeklyCheckIn?> = dao.getLatestCheckIn()
    val allEvolutionPhotos: Flow<List<EvolutionPhoto>> = dao.getAllEvolutionPhotos()
    val allCompletedLogs: Flow<List<ExerciseSetLog>> = dao.getAllCompletedLogs()

    suspend fun getUserProfileOnce(): UserProfile {
        return dao.getUserProfileOnce() ?: UserProfile()
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        dao.insertOrUpdateUserProfile(profile)
    }

    suspend fun getNextWorkoutTemplate(): WorkoutTemplate {
        val lastCompleted = dao.getLastCompletedSession()
        val nextCode = WorkoutCatalog.getNextWorkoutCode(lastCompleted?.workoutCode)
        return WorkoutCatalog.getWorkout(nextCode)
    }

    suspend fun getSessionById(sessionId: Long): WorkoutSession? {
        return dao.getSessionById(sessionId)
    }

    fun getLogsForSession(sessionId: Long): Flow<List<ExerciseSetLog>> {
        return dao.getLogsForSession(sessionId)
    }

    suspend fun getLogsForSessionOnce(sessionId: Long): List<ExerciseSetLog> {
        return dao.getLogsForSessionOnce(sessionId)
    }

    suspend fun getPreviousLogsForExercise(exerciseName: String, currentSessionId: Long): List<ExerciseSetLog> {
        return dao.getPreviousLogsForExercise(exerciseName, currentSessionId)
    }

    suspend fun startWorkoutSession(workoutCode: String): Long {
        val template = WorkoutCatalog.getWorkout(workoutCode)
        val now = System.currentTimeMillis()
        val session = WorkoutSession(
            workoutCode = template.code,
            title = "${template.title} — ${template.subtitle}",
            dateMillis = now,
            startTimeMillis = now,
            status = "EM_ANDAMENTO",
            completedExercisesCount = 0,
            totalExercisesCount = template.exercises.size,
            completedSetsCount = 0,
            cardioMinutes = 0
        )
        val sessionId = dao.insertSession(session)

        // Seed initial empty set logs for each exercise
        val logs = mutableListOf<ExerciseSetLog>()
        template.exercises.forEach { exercise ->
            // Try to find previous loads for this exercise
            val prevLogs = dao.getPreviousLogsForExercise(exercise.name, sessionId)
            val defaultWeight = prevLogs.firstOrNull()?.weightKg ?: 0f

            for (setIndex in 1..exercise.defaultSets) {
                val prevSet = prevLogs.find { it.setNumber == setIndex }
                logs.add(
                    ExerciseSetLog(
                        sessionId = sessionId,
                        workoutCode = workoutCode,
                        exerciseName = exercise.name,
                        setNumber = setIndex,
                        weightKg = prevSet?.weightKg ?: defaultWeight,
                        reps = prevSet?.reps ?: exercise.minReps,
                        rir = prevSet?.rir ?: 2,
                        completed = false
                    )
                )
            }
        }
        dao.insertSetLogs(logs)
        return sessionId
    }

    suspend fun updateSetLog(log: ExerciseSetLog) {
        dao.updateSetLog(log)
    }

    suspend fun completeWorkoutSession(
        sessionId: Long,
        cardioMinutes: Int,
        notes: String
    ) {
        val session = dao.getSessionById(sessionId) ?: return
        val logs = dao.getLogsForSessionOnce(sessionId)
        val completedSets = logs.count { it.completed }
        val distinctExercisesDone = logs.filter { it.completed }.map { it.exerciseName }.distinct().size

        val now = System.currentTimeMillis()
        val durationMin = maxOf(1, ((now - session.startTimeMillis) / (60 * 1000)).toInt())

        val updated = session.copy(
            endTimeMillis = now,
            durationMinutes = durationMin,
            status = "CONCLUIDO",
            completedExercisesCount = distinctExercisesDone,
            completedSetsCount = completedSets,
            cardioMinutes = cardioMinutes,
            notes = notes
        )
        dao.updateSession(updated)
    }

    suspend fun cancelWorkoutSession(sessionId: Long) {
        val session = dao.getSessionById(sessionId) ?: return
        dao.updateSession(session.copy(status = "CANCELADO"))
    }

    suspend fun addWeightRecord(weightKg: Float, notes: String, dateMillis: Long = System.currentTimeMillis()) {
        dao.insertWeightRecord(
            WeightRecord(
                dateMillis = dateMillis,
                weightKg = weightKg,
                notes = notes
            )
        )
        // Also update UserProfile current weight
        val profile = getUserProfileOnce()
        dao.insertOrUpdateUserProfile(profile.copy(currentWeightKg = weightKg))
    }

    suspend fun addBodyMeasurement(
        waistCm: Float,
        abdomenCm: Float,
        chestCm: Float,
        armCm: Float,
        thighCm: Float,
        notes: String = "",
        dateMillis: Long = System.currentTimeMillis()
    ) {
        dao.insertMeasurement(
            BodyMeasurement(
                dateMillis = dateMillis,
                waistCm = waistCm,
                abdomenCm = abdomenCm,
                chestCm = chestCm,
                armCm = armCm,
                thighCm = thighCm,
                notes = notes
            )
        )
        val profile = getUserProfileOnce()
        dao.insertOrUpdateUserProfile(profile.copy(currentWaistCm = waistCm))
    }

    suspend fun addWeeklyCheckIn(checkIn: WeeklyCheckIn) {
        dao.insertCheckIn(checkIn)
    }

    suspend fun addEvolutionPhoto(photo: EvolutionPhoto) {
        dao.insertEvolutionPhoto(photo)
    }

    suspend fun deleteSession(sessionId: Long) {
        dao.deleteSession(sessionId)
    }

    suspend fun deleteWeightRecord(id: Long) {
        dao.deleteWeightRecord(id)
    }

    suspend fun deleteMeasurement(id: Long) {
        dao.deleteMeasurement(id)
    }
}
