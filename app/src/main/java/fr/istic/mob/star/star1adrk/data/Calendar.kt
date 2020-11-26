package fr.istic.mob.star.star1adrk.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "calendar")
class Calendar (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "service_id") val serviceId: Int,
    val monday: String,
    val tuesday: String,
    val wednesday: String,
    val thursday: String,
    val friday: String,
    val saturday: String,
    val sunday: String,
    @ColumnInfo(name = "start_date") val startDate: Date?,
    @ColumnInfo(name = "end_date") val endDate: Date?
)