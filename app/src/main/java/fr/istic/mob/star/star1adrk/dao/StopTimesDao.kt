package fr.istic.mob.star.star1adrk.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.istic.mob.star.star1adrk.model.StopTime

@Dao
interface StopTimesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addStoptime(stop: StopTime)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addStoptimes(stoptimes: List<StopTime>)

    @Query("SELECT * FROM stop_times ORDER BY id ASC")
    fun getStoptimes(): LiveData<List<StopTime>>

    @Delete
    fun deleteStoptime(stoptimes: StopTime)

    @Delete
    fun deleteStoptimes(stoptimes: List<StopTime>)
}