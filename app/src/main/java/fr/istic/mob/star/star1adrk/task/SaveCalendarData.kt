package fr.istic.mob.star.star1adrk.task

import android.content.Context
import android.os.AsyncTask
import fr.istic.mob.star.star1adrk.database.AppDatabase
import fr.istic.mob.star.star1adrk.database.models.Calendar
import fr.istic.mob.star.star1adrk.utils.CALENDAR
import fr.istic.mob.star.star1adrk.utils.SaveDataCallbacks
import java.io.File

class SaveCalendarData(private var context: Context?, private var listener: SaveDataCallbacks) :
    AsyncTask<Void, Void, Boolean>() {

    override fun doInBackground(vararg params: Void?): Boolean {
        if (context != null)
            saveCalendarData(context!!)
        return true
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        if (result) {
            listener.onSaveCalendarComplete()
        }

    }

    private fun saveCalendarData(context: Context) {
        val lines: List<String> =
            File(context.getExternalFilesDir(null)?.path, CALENDAR).readLines()
        val calendarDao = AppDatabase.getInstance(context)?.calendarDao()
        for (i in 1 until lines.size) {
            val line = lines[i].replace("\"".toRegex(), "")
            val properties = line.split(",")
            calendarDao?.insert(
                Calendar(
                    id = null,
                    serviceId = properties[0],
                    monday = properties[1],
                    tuesday = properties[2],
                    wednesday = properties[3],
                    thursday = properties[4],
                    friday = properties[5],
                    saturday = properties[6],
                    sunday = properties[7],
                    startDate = properties[8],
                    endDate = properties[8],
                )
            )
        }
    }
}