package com.itstep.weatherapplication.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class Weather {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @ColumnInfo(name = "country") var country: String? = null
    @ColumnInfo(name = "temparuture")var temparute: String? = null
    @ColumnInfo(name = "temperatureMin")var temperatureMin: String? = null
    @ColumnInfo(name = "temperatureMax")var temperatureMax: String? = null
    @ColumnInfo(name = "humdity")var humdity: String? = null
    @ColumnInfo(name = "pressure")var pressure: String? = null


}