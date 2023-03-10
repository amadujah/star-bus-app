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

    @Query("SELECT DISTINCT(arrival_time) FROM stop_time st, trip t, calendar c where t.trip_id = st.trip_id and c.service_id = t.service_id and st.arrival_time >= :startTime and st.arrival_time < :stopTime and st.stop_id =:stopId and t.route_id =:routeId and CAST(c.end_date AS LONG)>= :startDate ORDER BY arrival_time ")
    fun getStopTimes(
        startTime: String?,
        stopTime: String?,
        routeId: String?,
        stopId: String?,
        startDate: Long? = 0
    ): Cursor
}
