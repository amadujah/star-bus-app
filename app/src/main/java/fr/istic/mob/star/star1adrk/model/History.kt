package fr.istic.mob.star.star1adrk.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "history")
class History (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "dataset_id") val datasetId: String,
    @ColumnInfo(name = "record_id") val recordId: String,
    val description: String,
    val url: String,
    @ColumnInfo(name = "debut_validite") val debutValidite: String,
    @ColumnInfo(name = "fin_validite") val finValidite: String,
    @ColumnInfo(name = "json_id") val jsonId: String,
)