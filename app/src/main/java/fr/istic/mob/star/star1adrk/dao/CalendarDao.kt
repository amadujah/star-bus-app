package fr.istic.mob.star.star1adrk.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.istic.mob.star.star1adrk.model.Calendar
import fr.istic.mob.star.star1adrk.model.relationships.CalendarWithTrips

@Dao
interface CalendarDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCalendar(calendar: Calendar)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCalendars(calendars: List<Calendar>)

    @Query("SELECT * FROM calendar ORDER BY service_id ASC")
    fun getCalendars(): LiveData<List<Calendar>>

    @Transaction
    @Query("SELECT * FROM calendar ORDER BY service_id ASC")
    fun getCalendarsWithTrips(): List<CalendarWithTrips>

    @Delete
    fun deleteCalendar(calendar: Calendar)

    @Delete
    fun deleteCalendars(calendars: List<Calendar>)
}