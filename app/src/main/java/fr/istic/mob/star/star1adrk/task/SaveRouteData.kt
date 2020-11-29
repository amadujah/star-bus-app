package fr.istic.mob.star.star1adrk.task

import android.content.Context
import android.os.AsyncTask
import fr.istic.mob.star.star1adrk.database.AppDatabase
import fr.istic.mob.star.star1adrk.database.models.Route
import fr.istic.mob.star.star1adrk.utils.ROUTES
import fr.istic.mob.star.star1adrk.utils.SaveDataCallbacks
import java.io.File

class SaveRouteData(private var context: Context?, private var listener: SaveDataCallbacks) : AsyncTask<Void, Void, Boolean>() {

    override fun doInBackground(vararg params: Void?): Boolean {
        if (context != null)
            saveRouteData(context!!)
        return true
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        if (result) {
            listener.onSaveRouteComplete()
        }
    }

    private fun saveRouteData(context: Context) {
        val lines: List<String> = File(context.getExternalFilesDir(null)?.path, ROUTES).readLines()
        val routeDao = AppDatabase.getInstance(context)?.routeDao()
        for (i in 1 until lines.size) {
            val line = lines[i].replace("\"".toRegex(), "")
            val properties = line.split(",")
            routeDao?.insert(
                Route(
                    routeId = properties[0],
                    routeShortName = properties[2],
                    routeLongName = properties[3],
                    routeDesc = properties[4],
                    routeType = properties[5],
                    routeColor = properties[7],
                    routeTextColor = properties[8],
                )
            )
        }
    }
}