package com.itstep.weatherapplication.dao

import android.content.Context
import androidx.room.RoomDatabase

import androidx.room.Database
import androidx.room.Room


@Database( entities = [Weather::class],
    version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun WeatherDao(): WeatherDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "weather.db")
            .allowMainThreadQueries()
            .build()
    }
}
