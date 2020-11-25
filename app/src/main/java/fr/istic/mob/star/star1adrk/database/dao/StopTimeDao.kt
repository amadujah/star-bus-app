package fr.istic.mob.star.star1adrk.database.dao

import android.database.Cursor

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.istic.mob.star.star1adrk.database.models.StopTime

@Dao
interface StopTimeDao {

    @Query("SELECT * FROM stoptime")
    fun loadAllStopTimes(): Cursor

    @Insert
    fun insertStopTime(stopTime: StopTime)

   @Query("DELETE FROM stoptime")
    fun delete()

   @Query("SELECT * FROM stoptime WHERE id = :id")
    fun loadStopTimeById(id: Int): StopTime

    @Query("SELECT * FROM stoptime WHERE trip_id = :segment1")
    fun getStopTimeTrip(segment1: StopTime): Cursor
}
