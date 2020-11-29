package fr.istic.mob.star.star1adrk.database.dao

import android.database.Cursor

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.istic.mob.star.star1adrk.database.models.Trip

@Dao
interface TripDao {

    @Query("SELECT * FROM trip ORDER BY trip_id")
    fun loadAllTrip(): List<Trip>

    @Insert
    fun insert(trip: Trip)

    @Query("DELETE FROM trip")
    fun deleteAll()

    @Query("SELECT * FROM trip WHERE trip.route_id = :selectionArgs0 AND trip.direction_id = :selectionArgs1")
    fun getTrips(selectionArgs0: String, selectionArgs1: String): List<Trip>
}
