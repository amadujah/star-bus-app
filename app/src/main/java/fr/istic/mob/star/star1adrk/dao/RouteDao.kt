package fr.istic.mob.star.star1adrk.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.istic.mob.star.star1adrk.data.Route

@Dao
interface RouteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRoute(route: Route)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRoutees(routes: List<Route>)

    @Query("SELECT * FROM route ORDER BY id ASC")
    fun getRoutes(): LiveData<List<Route>>

    @Delete
    fun deleteRoute(route: Route)

    @Delete
    fun deleteRoutes(routes: List<Route>)
}