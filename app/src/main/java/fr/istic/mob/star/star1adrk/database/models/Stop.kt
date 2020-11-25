package fr.istic.mob.star.star1adrk.database.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stop")
data class Stop(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "stop_id")
    val stopId: String,
    @ColumnInfo(name = "stop_name")
    val stop_name: String,
    @ColumnInfo(name = "stop_desc")
    val stop_desc: String,
    @ColumnInfo(name = "stop_lat")
    val stop_lat: String,
    @ColumnInfo(name = "stop_lon")
    val stop_lon: String,
    @ColumnInfo(name = "wheelchair_boarding")
    val wheelchair_boarding: String,
)
