package fr.istic.mob.star.star1adrk.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "stop_times")
class Stop_times (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "trip_id") val tripId: Int,
    @ColumnInfo(name = "service_id") val serviceId: Int,
    @ColumnInfo(name = "arrival_time") val arrivalTime: Date?,
    @ColumnInfo(name = "departure_time") val departureTime: Date?,
    @ColumnInfo(name = "stop_id") val stopId: Int,
    @ColumnInfo(name = "stop_headsign") val stopHeadsign: String,
    @ColumnInfo(name = "stop_sequence") val stopSequence: Int
)