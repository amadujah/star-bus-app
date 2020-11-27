package fr.istic.mob.star.star1adrk.model.relationships

import androidx.room.Relation
import fr.istic.mob.star.star1adrk.model.StopTime

class TripsWithStopTimes {
    @Relation(
        parentColumn = "id",
        entityColumn = "tripId"
    )
    val stopTimes: List<StopTime> = TODO()
}