package fr.istic.mob.star.star1adrk.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.istic.mob.star.star1adrk.database.models.*

@Database(
    entities = [DatabaseInfo::class, BusRoute::class, Calendar::class, Stop::class, StopTime::class, Trip::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseInfoDao(): DatabaseInfoDao

    companion object {
        var appInstance: AppDatabase? = null
        private val LOCK = Any()
        val DATABASE_NAME = "star_db"

        fun getInstance(context: Context): AppDatabase? {
            if (appInstance == null) {
                synchronized(LOCK) {
                    appInstance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, DATABASE_NAME
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return appInstance
        }
    }
}