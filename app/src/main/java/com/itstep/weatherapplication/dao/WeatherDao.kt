package com.itstep.weatherapplication.dao

import androidx.room.*


@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather")
    fun getAll(): List<Weather>

    @Query("SELECT * FROM weather WHERE id = :id")
    fun getById(id: Long): Weather?

    @Insert
    fun insert(employee: Weather?)

    @Update
    fun update(employee: Weather?)

    @Delete
    fun delete(employee: Weather?)
}