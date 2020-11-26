package fr.istic.mob.star.star1adrk.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.istic.mob.star.star1adrk.data.Trip

@Dao
interface TripDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTrip(trip: Trip)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTrips(trips: List<Trip>)

    @Query("SELECT * FROM trip ORDER BY id ASC")
    fun getTrips(): LiveData<List<Trip>>

    @Delete
    fun deleteTrip(trip: Trip)

    @Delete
    fun deleteTrips(trips: List<Trip>)
}