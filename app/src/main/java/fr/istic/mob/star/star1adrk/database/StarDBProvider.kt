package fr.istic.mob.star.star1adrk.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import fr.istic.mob.star.star1adrk.database.StarContract.BusRoutes
import fr.istic.mob.star.star1adrk.database.dao.*


class StarDBProvider : ContentProvider() {

    private var appDatabase: AppDatabase? = null

    private var routeDao: RouteDao? = null
    private var calendarDao: CalendarDao? = null
    private var stopDao: StopDao? = null
    private var stopTimeDao: StopTimeDao? = null
    private var tripDao: TripDao? = null

    private var cursor: Cursor? = null


    companion object {
        var uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH);
    }

    override fun onCreate(): Boolean {
        appDatabase = AppDatabase.getInstance(context!!)
        routeDao = appDatabase?.routeDao()
        calendarDao = appDatabase?.calendarDao()
        stopDao = appDatabase?.stopDao()
        stopTimeDao = appDatabase?.stopTimeDao()
        tripDao = appDatabase?.tripDao()

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {

        when (uri.toString()) {
            BusRoutes.CONTENT_URI.toString() -> {
                cursor = routeDao?.loadAllRoutes()
            }
            StarContract.Trips.CONTENT_URI.toString() -> {
                cursor = tripDao?.loadAllTrip()
            }
            StarContract.Stops.CONTENT_URI.toString() -> {
                cursor = stopDao?.getStops(selectionArgs?.get(0), selectionArgs?.get(1))
            }
            StarContract.StopTimes.CONTENT_URI.toString() -> {
                cursor = stopTimeDao?.getStopTimes(
                    startTime = selectionArgs?.get(0),
                    stopTime = selectionArgs?.get(1),
                    routeId = selectionArgs?.get(2),
                    stopId = selectionArgs?.get(3),
                    startDate = selectionArgs?.get(4)?.toLong(),
                )
            }

            StarContract.Calendar.CONTENT_URI.toString() -> {
                cursor = calendarDao?.getCalendars()
            }
        }

        return cursor
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            1 -> BusRoutes.CONTENT_TYPE
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}