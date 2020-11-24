package fr.istic.mob.star.star1adrk.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(DatabaseInfo::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseInfoDao(): DatabaseInfoDao
}