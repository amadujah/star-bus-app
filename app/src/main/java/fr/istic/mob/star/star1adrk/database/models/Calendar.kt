package fr.istic.mob.star.star1adrk.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "calendar")
data class Calendar(
    @PrimaryKey
    @ColumnInfo(name = "service_id") val serviceId: String,
    @ColumnInfo(name = "monday")
    val monday: String,
    @ColumnInfo(name = "tuesday")
    val tuesday: String,
    @ColumnInfo(name = "wednesday")
    val wednesday: String,
    @ColumnInfo(name = "thursday")
    val thursday: String,
    @ColumnInfo(name = "friday")
    val friday: String,
    @ColumnInfo(name = "saturday")
    val saturday: String,
    @ColumnInfo(name = "sunday")
    val sunday: String,
    @ColumnInfo(name = "start_date") val startDate: String?,
    @ColumnInfo(name = "end_date") val endDate: String?
)

