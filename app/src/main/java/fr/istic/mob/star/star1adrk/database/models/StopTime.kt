package fr.istic.mob.star.star1adrk.database.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stoptime")
data class StopTime(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "trip_id")
    val trip_id: String,
    @ColumnInfo(name = "arrival_time")
    val arrival_time: String,
    @ColumnInfo(name = "departure_time")
    val departure_time: String,
    @ColumnInfo(name = "stop_id")
    val stop_id: String,
    @ColumnInfo(name = "stop_sequence")
    val stop_sequence: String
)
