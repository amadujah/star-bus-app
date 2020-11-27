package fr.istic.mob.star.star1adrk.model.relationships

import androidx.room.Relation
import fr.istic.mob.star.star1adrk.model.StopTime

class StopsWithStopTimes {
    @Relation(
        parentColumn = "id",
        entityColumn = "stopId"
    )
    val stopTimes: List<StopTime> = TODO()
}