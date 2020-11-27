package fr.istic.mob.star.star1adrk.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "route")
data class Route(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "route_short_name") val routeShortName: String?,
    @ColumnInfo(name = "route_long_name") val routeLongName: String?,
    @ColumnInfo(name = "route_desc") val routeDesc: String?,
    @ColumnInfo(name = "route_type") val routeType: String?,
    @ColumnInfo(name = "route_url") val routeUrl: String?,
    @ColumnInfo(name = "route_color") val routeColor: String?,
    @ColumnInfo(name = "route_short_order") val routeShortOrder: String?,
    @ColumnInfo(name = "route_text_color") val routeTextColor: String?
){
    constructor(): this(
        null,
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
}