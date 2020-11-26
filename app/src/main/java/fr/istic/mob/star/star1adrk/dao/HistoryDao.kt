package fr.istic.mob.star.star1adrk.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.istic.mob.star.star1adrk.data.History

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addHistory(history: History)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addHistories(histories: List<History>)

    @Query("SELECT * FROM history ORDER BY id ASC")
    fun getHistories(): LiveData<List<History>>

    @Delete
    fun deleteHistory(History: History)

    @Delete
    fun deleteHistories(histories: List<History>)
}