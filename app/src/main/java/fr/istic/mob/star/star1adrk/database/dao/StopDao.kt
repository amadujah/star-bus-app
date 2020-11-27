package fr.istic.mob.star.star1adrk.database.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.istic.mob.star.star1adrk.database.models.Stop

@Dao
interface StopDao {

    @Query("SELECT * FROM stop")
    fun loadAllStops(): List<Stop>

    @Insert
    fun insert(stop: Stop)

    @Query("DELETE FROM stop")
    fun deleteAll()

    @Query("SELECT * FROM stop WHERE id = :stopId")
    fun loadStopByStopId(stopId: Int): Stop

    @Query(
        "SELECT * FROM stop, stop_time, trip WHERE stop.stop_id = stop_time.stop_id " +
                "AND stop_time.trip_id = trip.trip_id " +
                "AND trip.route_id = :selectionArgs0 AND trip.trip_id = :trip"
    )
    fun getStops(selectionArgs0: String, trip: String): Cursor
}
