package fr.istic.mob.star.star1adrk.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.istic.mob.star.star1adrk.database.models.History

@Dao
interface HistoryDao {
    @Query("Select * from history where recordid = :recordId")
    fun getHistoryById(recordId: String): History

    @Insert
    fun insertVersion(history: History)
}