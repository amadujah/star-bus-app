package fr.istic.mob.star.star1adrk.database.models

import com.google.gson.annotations.SerializedName

data class Version (
    @SerializedName("fields")
    val dbInfo: DatabaseInfo
)