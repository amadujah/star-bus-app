package fr.istic.mob.star.star1adrk.task

import android.content.Context
import android.os.AsyncTask
import fr.istic.mob.star.star1adrk.database.AppDatabase
import fr.istic.mob.star.star1adrk.database.models.Trip
import fr.istic.mob.star.star1adrk.utils.SaveDataCallbacks
import fr.istic.mob.star.star1adrk.utils.TRIPS
import java.io.File

class SaveTripData(private var context: Context?, private var listener: SaveDataCallbacks) : AsyncTask<Void, Void, Boolean>() {


    override fun doInBackground(vararg params: Void?): Boolean {
        if (context != null)
            saveTripData(context!!)
        return true
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        if (result) {
            listener.onSaveTripComplete()
        }
    }

    private fun saveTripData(context: Context) {
        val lines: List<String> = File(context.getExternalFilesDir(null)?.path, TRIPS).readLines()
        val tripDao = AppDatabase.getInstance(context)?.tripDao()
        for (i in 1 until lines.size) {
            val line = lines[i].replace("\"".toRegex(), "")
            val properties = line.split(",")

            tripDao?.insert(
                Trip(
                    routeId = properties[0],
                    serviceId = properties[1],
                    tripId = properties[2],
                    tripHeadSign = properties[3],
                    directionId = properties[5],
                    blockId = properties[6],
                    wheelchairAccessible = properties[8],
                )
            )
        }
    }
}