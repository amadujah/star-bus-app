package fr.istic.mob.star.star1adrk.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.istic.mob.star.star1adrk.database.dao.*
import fr.istic.mob.star.star1adrk.database.models.*
import fr.istic.mob.star.star1adrk.utils.DatabaseInfoConverter
import fr.istic.mob.star.star1adrk.utils.DateConverters

@Database(
    entities = [DatabaseInfo::class, History::class, Route::class, Calendar::class, Stop::class, StopTime::class, Trip::class],
    version = 2
)
@TypeConverters(DatabaseInfoConverter::class, DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseInfoDao(): DatabaseInfoDao
    abstract fun historyDao(): HistoryDao
    abstract fun routeDao(): RouteDao
    abstract fun tripDao(): TripDao
    abstract fun stopDao(): StopDao
    abstract fun stopTimeDao(): StopTimeDao
    abstract fun calendarDao(): CalendarDao

    companion object {
        var appInstance: AppDatabase? = null
        private val LOCK = Any()
        val DATABASE_NAME = "star.db"

        fun getInstance(context: Context): AppDatabase? {
            if (appInstance == null) {
                synchronized(LOCK) {
                    appInstance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, DATABASE_NAME
                    )
                        .allowMainThreadQueries() //ou le mettre dans une task
                        .build()
                }
            }
            return appInstance
        }
    }
}