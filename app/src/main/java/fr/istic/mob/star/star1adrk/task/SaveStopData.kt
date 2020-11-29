package fr.istic.mob.star.star1adrk.task

import android.content.Context
import android.os.AsyncTask
import fr.istic.mob.star.star1adrk.database.AppDatabase
import fr.istic.mob.star.star1adrk.database.models.Stop
import fr.istic.mob.star.star1adrk.utils.STOPS
import fr.istic.mob.star.star1adrk.utils.SaveDataCallbacks
import java.io.File

class SaveStopData(private var context: Context?, private var listener: SaveDataCallbacks) :
    AsyncTask<Void, Void, Boolean>() {


    override fun doInBackground(vararg params: Void?): Boolean {
        if (context != null)
            saveStopData(context!!)
        return true
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        if (result) {
            listener.onSaveStopComplete()
        }
    }

    private fun saveStopData(context: Context) {
        val lines: List<String> = File(context.getExternalFilesDir(null)?.path, STOPS).readLines()
        val stopDao = AppDatabase.getInstance(context)?.stopDao()
        for (i in 1 until lines.size) {
            val line = lines[i].replace("\"".toRegex(), "")
            val properties = line.split(",")

            stopDao?.insert(
                Stop(
                    stopId = properties[0],
                    stopName = properties[2],
                    stopDesc = properties[3],
                    stopLat = properties[4],
                    stopLon = properties[5],
                    wheelchairBoarding = properties[11],
                )
            )
        }
    }
}