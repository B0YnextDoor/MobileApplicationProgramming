package com.example.tabatatimer.android

import android.app.Application
import androidx.room.Room
import com.example.tabatatimer.android.domain.AppDatabase

class TabataApplication : Application() {

    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "workout-database"
        ).build()
    }
}