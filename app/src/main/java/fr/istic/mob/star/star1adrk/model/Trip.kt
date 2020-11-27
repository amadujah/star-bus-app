package fr.istic.mob.star.star1adrk.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip")
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "route_id") val routeId: Int?,
    @ColumnInfo(name = "service_id") val serviceId: Int?,
    @ColumnInfo(name = "trip_headsign") val tripHeadsign: String?,
    @ColumnInfo(name = "direction_id") val directionId: Int?
){
    constructor(): this(
        null,
        null,
        null,
        "",
        null
    )
}