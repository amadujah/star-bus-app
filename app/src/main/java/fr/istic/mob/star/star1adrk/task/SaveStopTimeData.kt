package fr.istic.mob.star.star1adrk.task

import android.content.Context
import android.os.AsyncTask
import fr.istic.mob.star.star1adrk.database.AppDatabase
import fr.istic.mob.star.star1adrk.database.models.StopTime
import fr.istic.mob.star.star1adrk.utils.STOP_TIMES
import fr.istic.mob.star.star1adrk.utils.SaveDataCallbacks
import java.io.File

class SaveStopTimeData(private var context: Context?, private var listener: SaveDataCallbacks) :
    AsyncTask<Void, Void, Boolean>() {


    override fun doInBackground(vararg params: Void?): Boolean {
        if (context != null)
            saveStopTimeData(context!!)
        return true
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        if (result) {
            listener.onSaveStopTimeComplete()
        }

    }

    private fun saveStopTimeData(context: Context) {
        val lines: List<String> =
            File(context.getExternalFilesDir(null)?.path, STOP_TIMES).readLines()
        val stopTimeDao = AppDatabase.getInstance(context)?.stopTimeDao()
        for (i in 1 until lines.size) {
            val line = lines[i].replace("\"".toRegex(), "")
            val properties = line.split(",")
            stopTimeDao?.insert(
                StopTime(
                    id = null,
                    tripId = properties[0],
                    arrivalTime = properties[1],
                    departureTime = properties[2],
                    stopId = properties[3],
                    stopSequence = properties[4],
                )
            )
        }
    }
}