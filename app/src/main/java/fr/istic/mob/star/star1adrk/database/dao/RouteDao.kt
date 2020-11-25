package fr.istic.mob.star.star1adrk.database.dao

import android.database.Cursor

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.istic.mob.star.star1adrk.database.models.BusRoute

@Dao
interface RouteDao {

    @Query("SELECT * FROM busroute ORDER BY route_id")
    fun loadAllRoutes(): Cursor

    @Query("SELECT route_short_name FROM busroute ORDER BY route_id")
    fun loadAllRouteShortName(): List<String>

    @Insert
    fun insertRoute(busRoute: BusRoute)

    @Query("DELETE FROM busroute")
    fun delete()

    @Query("SELECT route_long_name FROM busroute ORDER BY id")
    fun loadRouteByDirectory(): List<String>
}
