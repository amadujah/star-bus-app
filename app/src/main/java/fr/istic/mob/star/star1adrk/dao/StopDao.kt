package fr.istic.mob.star.star1adrk.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.istic.mob.star.star1adrk.data.Stop

@Dao
interface StopDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addStop(stop: Stop)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addStops(stops: List<Stop>)

    @Query("SELECT * FROM stop ORDER BY id ASC")
    fun getStops(): LiveData<List<Stop>>

    @Delete
    fun deleteStop(stop: Stop)

    @Delete
    fun deleteStops(stops: List<Stop>)
}