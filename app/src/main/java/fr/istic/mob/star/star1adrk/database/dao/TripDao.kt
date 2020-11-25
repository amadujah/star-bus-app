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
    fun insertTrip(trip: Trip)

    @Query("DELETE FROM trip")
    fun delete()

    @Query("SELECT * FROM trip WHERE trip_id = :tripId")
    fun loadTripByTripId(tripId: Int): Trip

    @Query("SELECT * FROM trip WHERE trip.route_id = :selectionArgs0 AND trip.direction_id = :selectionArgs1")
    fun getTrips(selectionArgs0: String, selectionArgs1: String): Cursor

    @Query("SELECT * FROM trip WHERE route_id = :segment1")
    fun load(segment1: String): Cursor
}
