package fr.istic.mob.star.star1adrk.database.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stop_time")
data class StopTime(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "trip_id")
    val tripId: String,
    @ColumnInfo(name = "arrival_time")
    val arrivalTime: String,
    @ColumnInfo(name = "departure_time")
    val departureTime: String,
    @ColumnInfo(name = "stop_id")
    val stopId: String,
    @ColumnInfo(name = "stop_sequence")
    val stopSequence: String
)
