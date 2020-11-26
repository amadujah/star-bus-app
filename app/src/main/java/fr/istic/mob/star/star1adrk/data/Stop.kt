package fr.istic.mob.star.star1adrk.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stop")
class Stop (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "stop_code") val stopCode: String,
    @ColumnInfo(name = "stop_name") val stopName: String,
    @ColumnInfo(name = "stop_desc") val stopDesc: String,
    @ColumnInfo(name = "stop_lat") val stopLat: String,
    @ColumnInfo(name = "stop_lon") val stopLon: String,
)