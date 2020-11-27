package fr.istic.mob.star.star1adrk.database.dao

import androidx.room.*
import fr.istic.mob.star.star1adrk.database.models.Calendar

@Dao
interface CalendarDao {
    @Insert
    fun insert(calendar: Calendar)

    @Query("DELETE FROM calendar")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCalendar(calendar: Calendar)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCalendars(calendars: List<Calendar>)

    @Query("SELECT * FROM calendar ORDER BY service_id ASC")
    fun getCalendars(): List<Calendar>

    @Delete
    fun deleteCalendar(calendar: Calendar)
}
