package fr.istic.mob.star.star1adrk.model.relationships

import androidx.room.Relation
import fr.istic.mob.star.star1adrk.model.Trip

class CalendarWithTrips {
    @Relation(
        parentColumn = "serviceId",
        entityColumn = "serviceId"
    )
    val trips: List<Trip> = TODO()
}