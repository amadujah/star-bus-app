package fr.istic.mob.star.star1adrk.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "route_id")
    val routeId: String,
    @ColumnInfo(name = "service_id")
    val serviceId: String,
    @ColumnInfo(name = "trip_id")
    val trip_id: String,
    @ColumnInfo(name = "trip_headsign")
    val trip_headsign: String,
    @ColumnInfo(name = "direction_id")
    val direction_id: String,
    @ColumnInfo(name = "block_id")
    val block_id: String,
    @ColumnInfo(name = "wheelchair_accessible")
    val wheelchair_accessible: String
)
