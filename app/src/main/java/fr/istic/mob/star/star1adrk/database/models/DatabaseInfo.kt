package fr.istic.mob.star.star1adrk.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class DatabaseInfo(
    @SerializedName("id")
    @PrimaryKey val id: String,
    @SerializedName("debutvalidite")
    @ColumnInfo(name = "debutvalidite") val validityStart: String,
    @SerializedName("finvalidite")
    @ColumnInfo(name = "finvalidite") val validityEnd: String,
    @SerializedName("url")
    @ColumnInfo(name = "url") val url: String
)