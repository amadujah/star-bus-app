package fr.istic.mob.star.star1adrk.repository

import androidx.lifecycle.LiveData
import fr.istic.mob.star.star1adrk.dao.RouteDao
import fr.istic.mob.star.star1adrk.data.Route

class RouteRepository(private val routeDao: RouteDao) {

    val getRoutes: LiveData<List<Route>> = routeDao.getRoutes()

    fun addRoutes(routes: List<Route>){
        routeDao.addRoutees(routes)
    }
}