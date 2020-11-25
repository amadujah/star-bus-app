package fr.istic.mob.star.star1adrk.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.istic.mob.star.star1adrk.database.models.Calendar

@Dao
interface CalendarDao {

    @Query("SELECT * FROM calendar ORDER BY id")
    fun loadAllCalendar(): List<Calendar>

    @Insert
    fun insertCalendar(calendar: Calendar)

    @Query("DELETE FROM calendar")
    fun delete()
}
