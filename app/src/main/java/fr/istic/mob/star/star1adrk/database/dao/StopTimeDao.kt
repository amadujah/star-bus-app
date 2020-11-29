package fr.istic.mob.star.star1adrk.database.dao

import android.database.Cursor

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.istic.mob.star.star1adrk.database.models.StopTime

@Dao
interface StopTimeDao {

    @Query("SELECT * FROM stop_time")
    fun loadAllStopTimes(): List<StopTime>

    @Insert
    fun insert(stopTime: StopTime)

   @Query("DELETE FROM stop_time")
    fun deleteAll()
}
