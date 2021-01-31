package fr.istic.mob.star.star1adrk.database.dao

import android.database.Cursor
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
    fun getCalendars(): Cursor

    @Delete
    fun deleteCalendar(calendar: Calendar)
}
