package fr.istic.mob.star.star1adrk.database.dao

import android.database.Cursor

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.istic.mob.star.star1adrk.database.models.Route

@Dao
interface RouteDao {

    @Query("SELECT * FROM route ORDER BY route_id")
    fun loadAllRoutes(): Cursor

    @Query("SELECT route_short_name FROM route ORDER BY route_id")
    fun loadAllRouteShortName(): Cursor

    @Insert
    fun insert(route: Route)

    @Query("DELETE FROM route")
    fun deleteAll()
}
