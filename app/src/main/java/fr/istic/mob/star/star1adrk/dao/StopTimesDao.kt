package fr.istic.mob.star.star1adrk.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.istic.mob.star.star1adrk.model.Stop_times

@Dao
interface StopTimesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addStoptime(stop: Stop_times)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addStoptimes(stoptimes: List<Stop_times>)

    @Query("SELECT * FROM stop_times ORDER BY id ASC")
    fun getStoptimes(): LiveData<List<Stop_times>>

    @Delete
    fun deleteStoptime(stoptimes: Stop_times)

    @Delete
    fun deleteStoptimes(stoptimes: List<Stop_times>)
}