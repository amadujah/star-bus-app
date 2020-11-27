package fr.istic.mob.star.star1adrk.model.relationships

import androidx.room.Relation
import fr.istic.mob.star.star1adrk.model.Trip

class RouteWithTrips {
    @Relation(
        parentColumn = "id",
        entityColumn = "routeId"
    )
    val trips: List<Trip> = TODO()
}