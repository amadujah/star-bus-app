package fr.istic.mob.star.star1adrk.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @SerializedName("fields")
    val dbInfo: DatabaseInfo,
    @SerializedName("recordid")
    val recordId: String
) {
}