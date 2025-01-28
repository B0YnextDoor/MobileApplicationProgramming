package com.example.tabatatimer.android.domain

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

enum class PHASE {
    Preparing,
    Working,
    Rest,
    Cooldown,
    Finish
}

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "color") val color: Int,
    @ColumnInfo(name = "prep_duration", defaultValue = "15") val prepDuration: Int = 15,
    @ColumnInfo(name = "work_duration", defaultValue = "60") val workDuration: Int = 60,
    @ColumnInfo(name = "rest_duration", defaultValue = "120") val restDuration: Int = 120,
    @ColumnInfo(name = "cooldown_duration", defaultValue = "300") val cooldownDuration: Int = 300,
    @ColumnInfo(name = "work_circles", defaultValue = "8") val workCircles: Int = 8,
    @ColumnInfo(name = "repeat_rest", defaultValue = "0") val repeatRest: Int = 0,
    @ColumnInfo(name = "total_repeats", defaultValue = "1") val totalRepeats: Int = 1
)

@Dao
interface WorkoutDao {

    @Insert
    suspend fun insert(workout: Workout)

    @Update
    suspend fun update(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)

    @Query("SELECT * FROM workouts")
    suspend fun getAllWorkouts(): List<Workout>

    @Query("DELETE FROM workouts")
    suspend fun deleteAllWorkouts()
}
