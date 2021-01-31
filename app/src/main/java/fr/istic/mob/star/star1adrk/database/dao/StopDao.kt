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

    @Query("SELECT * FROM stop WHERE stop_id = :stopId")
    fun loadStopByStopId(stopId: String): Stop

    @Query("SELECT DISTINCT s.stop_name, s.stop_id FROM stop s, stop_time st, trip t WHERE s.stop_id = st.stop_id AND st.trip_id = t.trip_id AND t.route_id = :routeId and direction_id = :directionId ORDER by CAST(st.stop_sequence AS INTEGER)")
    fun getStops(routeId: String?, directionId: String?): Cursor
}
