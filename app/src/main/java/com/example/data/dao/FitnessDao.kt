package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.BodyMeasurement
import com.example.data.entity.EvolutionPhoto
import com.example.data.entity.ExerciseSetLog
import com.example.data.entity.UserProfile
import com.example.data.entity.WeeklyCheckIn
import com.example.data.entity.WeightRecord
import com.example.data.entity.WorkoutSession
import kotlinx.coroutines.flow.Flow

@Dao
interface FitnessDao {

    // --- User Profile ---
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserProfile(profile: UserProfile)

    // --- Workout Sessions ---
    @Query("SELECT * FROM workout_sessions ORDER BY startTimeMillis DESC")
    fun getAllSessions(): Flow<List<WorkoutSession>>

    @Query("SELECT * FROM workout_sessions WHERE status = 'CONCLUIDO' ORDER BY endTimeMillis DESC")
    fun getCompletedSessions(): Flow<List<WorkoutSession>>

    @Query("SELECT * FROM workout_sessions WHERE status = 'EM_ANDAMENTO' ORDER BY startTimeMillis DESC LIMIT 1")
    fun getActiveSession(): Flow<WorkoutSession?>

    @Query("SELECT * FROM workout_sessions WHERE id = :sessionId LIMIT 1")
    suspend fun getSessionById(sessionId: Long): WorkoutSession?

    @Query("SELECT * FROM workout_sessions WHERE status = 'CONCLUIDO' ORDER BY endTimeMillis DESC LIMIT 1")
    suspend fun getLastCompletedSession(): WorkoutSession?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: WorkoutSession): Long

    @Update
    suspend fun updateSession(session: WorkoutSession)

    @Query("DELETE FROM workout_sessions WHERE id = :sessionId")
    suspend fun deleteSession(sessionId: Long)

    // --- Exercise Set Logs ---
    @Query("SELECT * FROM exercise_set_logs WHERE sessionId = :sessionId ORDER BY setNumber ASC")
    fun getLogsForSession(sessionId: Long): Flow<List<ExerciseSetLog>>

    @Query("SELECT * FROM exercise_set_logs WHERE sessionId = :sessionId ORDER BY setNumber ASC")
    suspend fun getLogsForSessionOnce(sessionId: Long): List<ExerciseSetLog>

    @Query("SELECT * FROM exercise_set_logs WHERE exerciseName = :exerciseName AND completed = 1 ORDER BY timestamp DESC LIMIT 20")
    fun getCompletedLogsForExercise(exerciseName: String): Flow<List<ExerciseSetLog>>

    @Query("SELECT * FROM exercise_set_logs WHERE exerciseName = :exerciseName AND completed = 1 AND sessionId != :currentSessionId ORDER BY timestamp DESC")
    suspend fun getPreviousLogsForExercise(exerciseName: String, currentSessionId: Long): List<ExerciseSetLog>

    @Query("SELECT * FROM exercise_set_logs WHERE completed = 1 ORDER BY timestamp ASC")
    fun getAllCompletedLogs(): Flow<List<ExerciseSetLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetLog(log: ExerciseSetLog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetLogs(logs: List<ExerciseSetLog>)

    @Update
    suspend fun updateSetLog(log: ExerciseSetLog)

    @Query("DELETE FROM exercise_set_logs WHERE id = :logId")
    suspend fun deleteSetLog(logId: Long)

    // --- Weight Records ---
    @Query("SELECT * FROM weight_records ORDER BY dateMillis DESC")
    fun getAllWeightRecords(): Flow<List<WeightRecord>>

    @Query("SELECT * FROM weight_records ORDER BY dateMillis DESC")
    suspend fun getAllWeightRecordsOnce(): List<WeightRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightRecord(record: WeightRecord): Long

    @Query("DELETE FROM weight_records WHERE id = :id")
    suspend fun deleteWeightRecord(id: Long)

    // --- Body Measurements ---
    @Query("SELECT * FROM body_measurements ORDER BY dateMillis DESC")
    fun getAllMeasurements(): Flow<List<BodyMeasurement>>

    @Query("SELECT * FROM body_measurements ORDER BY dateMillis DESC LIMIT 1")
    fun getLatestMeasurement(): Flow<BodyMeasurement?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurement: BodyMeasurement): Long

    @Query("DELETE FROM body_measurements WHERE id = :id")
    suspend fun deleteMeasurement(id: Long)

    // --- Weekly Check-ins ---
    @Query("SELECT * FROM weekly_checkins ORDER BY dateMillis DESC")
    fun getAllCheckIns(): Flow<List<WeeklyCheckIn>>

    @Query("SELECT * FROM weekly_checkins ORDER BY dateMillis DESC LIMIT 1")
    fun getLatestCheckIn(): Flow<WeeklyCheckIn?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: WeeklyCheckIn): Long

    @Query("DELETE FROM weekly_checkins WHERE id = :id")
    suspend fun deleteCheckIn(id: Long)

    // --- Evolution Photos ---
    @Query("SELECT * FROM evolution_photos ORDER BY dateMillis DESC")
    fun getAllEvolutionPhotos(): Flow<List<EvolutionPhoto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvolutionPhoto(photo: EvolutionPhoto): Long

    @Query("DELETE FROM evolution_photos WHERE id = :id")
    suspend fun deleteEvolutionPhoto(id: Long)
}
