package com.example.tabatatimer.android.domain

class WorkoutRepository(private val workoutDao: WorkoutDao) {

    suspend fun insertWorkout(workout: Workout) {
        workoutDao.insert(workout)
    }

    suspend fun updateWorkout(workout: Workout) {
        workoutDao.update(workout)
    }

    suspend fun deleteWorkout(workout: Workout) {
        workoutDao.delete(workout)
    }

    suspend fun getWorkouts(): List<Workout> {
        return workoutDao.getAllWorkouts()
    }

    suspend fun clearAll() {
        return workoutDao.deleteAllWorkouts()
    }
}