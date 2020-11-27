package fr.istic.mob.star.star1adrk.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.istic.mob.star.star1adrk.model.Stop
import fr.istic.mob.star.star1adrk.model.relationships.StopsWithStopTimes

@Dao
interface StopDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addStop(stop: Stop)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addStops(stops: List<Stop>)

    @Query("SELECT * FROM stop ORDER BY id ASC")
    fun getStops(): LiveData<List<Stop>>

    @Transaction
    @Query("SELECT * FROM stop")
    fun getStopsWithStopTimes(): List<StopsWithStopTimes>

    @Delete
    fun deleteStop(stop: Stop)

    @Delete
    fun deleteStops(stops: List<Stop>)
}