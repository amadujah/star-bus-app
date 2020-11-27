package fr.istic.mob.star.star1adrk.database

import android.content.Context
import androidx.room.*
import fr.istic.mob.star.star1adrk.Utils.DateConverters
import fr.istic.mob.star.star1adrk.dao.*
import fr.istic.mob.star.star1adrk.model.*

@Database(entities = [Calendar::class, History::class, Route::class, Stop::class, Stop_times::class, Trip::class],
          version = 1, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class StarDatabase : RoomDatabase() {

    abstract fun calendarDao(): CalendarDao
    abstract fun historyDao(): HistoryDao
    abstract fun routeDao(): RouteDao
    abstract fun stopDao(): StopDao
    abstract fun stopTimesDao(): StopTimesDao
    abstract fun tripDao(): TripDao

    companion object {
        @Volatile
        private var INSTANCE: StarDatabase? = null

        fun getDatabase(context: Context): StarDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StarDatabase::class.java,
                    "star.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}