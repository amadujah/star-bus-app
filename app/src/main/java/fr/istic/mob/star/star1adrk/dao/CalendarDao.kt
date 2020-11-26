package fr.istic.mob.star.star1adrk.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.istic.mob.star.star1adrk.data.Calendar

@Dao
interface CalendarDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCalendar(calendar: Calendar)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCalendars(calendars: List<Calendar>)

    @Query("SELECT * FROM calendar ORDER BY service_id ASC")
    fun getCalendars(): LiveData<List<Calendar>>

    @Delete
    fun deleteCalendar(calendar: Calendar)

    @Delete
    fun deleteCalendars(calendars: List<Calendar>)
}