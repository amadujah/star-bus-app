package fr.istic.mob.star.star1adrk.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar")
data class Calendar(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "service_id")
    val serviceId: String,
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
    @ColumnInfo(name = "start_date")
    val start_date: String,
    @ColumnInfo(name = "end_date")
    val end_date: String
)
