package fr.istic.mob.star.star1adrk.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip")
data class Trip(
    @ColumnInfo(name = "route_id")
    val routeId: String,
    @ColumnInfo(name = "service_id")
    val serviceId: String,
    @PrimaryKey
    @ColumnInfo(name = "trip_id")
    val tripId: String,
    @ColumnInfo(name = "trip_headsign")
    val tripHeadSign: String,
    @ColumnInfo(name = "direction_id")
    val directionId: String,
    @ColumnInfo(name = "block_id")
    val blockId: String,
    @ColumnInfo(name = "wheelchair_accessible")
    val wheelchairAccessible: String
)
