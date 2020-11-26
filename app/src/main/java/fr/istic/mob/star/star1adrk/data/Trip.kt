package fr.istic.mob.star.star1adrk.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip")
class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "route_id") val routeId: Int,
    @ColumnInfo(name = "service_id") val serviceId: Int,
    @ColumnInfo(name = "trip_headsign") val tripHeadsign: String,
    @ColumnInfo(name = "direction_id") val directionId: Int
)