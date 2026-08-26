package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.WorkoutCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Treino & Evolução", appName)
    }

    @Test
    fun `verify workout catalog sequence A to B to C`() {
        val nextAfterA = WorkoutCatalog.getNextWorkoutCode("A")
        assertEquals("B", nextAfterA)

        val nextAfterB = WorkoutCatalog.getNextWorkoutCode("B")
        assertEquals("C", nextAfterB)

        val nextAfterC = WorkoutCatalog.getNextWorkoutCode("C")
        assertEquals("A", nextAfterC)
    }

    @Test
    fun `verify Treino A exercises count and presence`() {
        val workoutA = WorkoutCatalog.TREINO_A
        assertEquals(7, workoutA.exercises.size)
        assertEquals("Leg Press 45°", workoutA.exercises.first().name)
    }
}

