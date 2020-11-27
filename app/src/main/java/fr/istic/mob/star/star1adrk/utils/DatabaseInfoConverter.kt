package fr.istic.mob.star.star1adrk.utils

import androidx.room.TypeConverter
import fr.istic.mob.star.star1adrk.database.models.DatabaseInfo
import org.json.JSONObject

class DatabaseInfoConverter {
    @TypeConverter
    fun fromSource(databaseInfo: DatabaseInfo): String {
        return JSONObject().apply {
            put("id", databaseInfo.id)
            put("debutvalidite", databaseInfo.validityStart)
            put("finvalidite", databaseInfo.validityEnd)
            put("url", databaseInfo.url)
        }.toString()
    }

    @TypeConverter
    fun toSource(databaseInfo: String): DatabaseInfo {
        val json = JSONObject(databaseInfo)
        return DatabaseInfo(id = json.getString("id"),
            validityStart = json.getString("debutvalidite"),
            validityEnd = json.getString("finvalidite"),
            url = json.getString("url"),
        )
    }
}