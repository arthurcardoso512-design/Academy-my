package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.FitnessDao
import com.example.data.entity.BodyMeasurement
import com.example.data.entity.EvolutionPhoto
import com.example.data.entity.ExerciseSetLog
import com.example.data.entity.UserProfile
import com.example.data.entity.WeeklyCheckIn
import com.example.data.entity.WeightRecord
import com.example.data.entity.WorkoutSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfile::class,
        WorkoutSession::class,
        ExerciseSetLog::class,
        WeightRecord::class,
        BodyMeasurement::class,
        WeeklyCheckIn::class,
        EvolutionPhoto::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun fitnessDao(): FitnessDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "treino_evolucao_db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.fitnessDao())
                }
            }
        }

        suspend fun populateInitialData(dao: FitnessDao) {
            // Initial Profile
            val initialProfile = UserProfile(
                id = 1,
                name = "Arthur",
                initialWeightKg = 122.0f,
                currentWeightKg = 122.0f,
                targetWeightKg = 100.0f,
                initialWaistCm = 120.0f,
                currentWaistCm = 120.0f,
                startDateMillis = System.currentTimeMillis() - 7L * 24 * 3600 * 1000, // 1 week ago
                unlockedTreinoD = false
            )
            dao.insertOrUpdateUserProfile(initialProfile)

            // Initial Weight Record (122 kg)
            val now = System.currentTimeMillis()
            val dayMillis = 24L * 3600 * 1000
            dao.insertWeightRecord(
                WeightRecord(
                    dateMillis = now - 7 * dayMillis,
                    weightKg = 122.0f,
                    notes = "Pesagem inicial de retorno à musculação"
                )
            )
            dao.insertWeightRecord(
                WeightRecord(
                    dateMillis = now - 3 * dayMillis,
                    weightKg = 121.2f,
                    notes = "Progresso constante mantendo dieta e água"
                )
            )

            // Initial Body Measurements
            dao.insertMeasurement(
                BodyMeasurement(
                    dateMillis = now - 7 * dayMillis,
                    waistCm = 120.0f,
                    abdomenCm = 124.0f,
                    chestCm = 118.0f,
                    armCm = 40.0f,
                    thighCm = 68.0f,
                    notes = "Medidas iniciais"
                )
            )
            dao.insertMeasurement(
                BodyMeasurement(
                    dateMillis = now - 1 * dayMillis,
                    waistCm = 118.5f,
                    abdomenCm = 122.0f,
                    chestCm = 118.5f,
                    armCm = 40.5f,
                    thighCm = 68.0f,
                    notes = "Evolução da cintura (-1.5 cm)!"
                )
            )

            // Initial Sample completed session to populate historical load & next workout A -> B
            val pastSessionId = dao.insertSession(
                WorkoutSession(
                    workoutCode = "A",
                    title = "Treino A — Base de força",
                    dateMillis = now - 2 * dayMillis,
                    startTimeMillis = now - 2 * dayMillis,
                    endTimeMillis = now - 2 * dayMillis + 58 * 60 * 1000,
                    durationMinutes = 58,
                    status = "CONCLUIDO",
                    completedExercisesCount = 7,
                    totalExercisesCount = 7,
                    completedSetsCount = 17,
                    cardioMinutes = 8,
                    notes = "Treino concluído com foco na técnica e RIR."
                )
            )

            // Seed some past exercise set logs for historical performance comparisons
            dao.insertSetLog(
                ExerciseSetLog(
                    sessionId = pastSessionId,
                    workoutCode = "A",
                    exerciseName = "Leg Press 45°",
                    setNumber = 1,
                    weightKg = 100f,
                    reps = 10,
                    rir = 3,
                    completed = true,
                    timestamp = now - 2 * dayMillis
                )
            )
            dao.insertSetLog(
                ExerciseSetLog(
                    sessionId = pastSessionId,
                    workoutCode = "A",
                    exerciseName = "Leg Press 45°",
                    setNumber = 2,
                    weightKg = 100f,
                    reps = 10,
                    rir = 3,
                    completed = true,
                    timestamp = now - 2 * dayMillis
                )
            )
            dao.insertSetLog(
                ExerciseSetLog(
                    sessionId = pastSessionId,
                    workoutCode = "A",
                    exerciseName = "Leg Press 45°",
                    setNumber = 3,
                    weightKg = 100f,
                    reps = 9,
                    rir = 3,
                    completed = true,
                    timestamp = now - 2 * dayMillis
                )
            )
            dao.insertSetLog(
                ExerciseSetLog(
                    sessionId = pastSessionId,
                    workoutCode = "A",
                    exerciseName = "Supino Máquina ou Halteres",
                    setNumber = 1,
                    weightKg = 40f,
                    reps = 10,
                    rir = 2,
                    completed = true,
                    timestamp = now - 2 * dayMillis
                )
            )

            // Sample Weekly CheckIn
            dao.insertCheckIn(
                WeeklyCheckIn(
                    dateMillis = now - 1 * dayMillis,
                    weekNumber = 1,
                    avgWeightKg = 121.6f,
                    avgSteps = 7500,
                    workoutsCompleted = 3,
                    energyScore = 8,
                    sleepScore = 7,
                    hungerScore = 5,
                    hasPain = false,
                    painLocation = "",
                    notes = "Semana produtiva, boa adaptação ao volume de treino.",
                    summaryFeedback = "Esta semana você realizou 3 de 3 treinos. Seu peso apresentou tendência de queda. Você manteve sua consistência."
                )
            )
        }
    }
}
