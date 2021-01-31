package fr.istic.mob.star.star1adrk.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import fr.istic.mob.star.star1adrk.database.AppDatabase
import fr.istic.mob.star.star1adrk.task.*
import fr.istic.mob.star.star1adrk.utils.SaveDataCallbacks
import fr.istic.mob.star.star1adrk.utils.getHistoriesFromJson


class SaveDBService : IntentService("SaveDBService"), SaveDataCallbacks {

    override fun onHandleIntent(intent: Intent?) {
        startDownload(this)
    }

    companion object {
        fun getDownloadService(
            callingClassContext: Context
        ): Intent {
            return Intent(callingClassContext, SaveDBService::class.java)
        }
    }

    private fun startDownload(context: Context) {
        val db = AppDatabase.getInstance(context)
        //Clear database previous content
        db?.routeDao()?.deleteAll()
        db?.stopDao()?.deleteAll()
        db?.tripDao()?.deleteAll()
        db?.stopTimeDao()?.deleteAll()
        db?.calendarDao()?.deleteAll()

        SaveCalendarData(context = this, listener = this).execute()
        SaveRouteData(context = this, listener = this).execute()
        //save history
        val historyDao = db?.historyDao()
        val databaseHistories = getHistoriesFromJson(this)
        databaseHistories?.forEach {
            historyDao?.insert(it)
        }

        SaveTripData(context = this, listener = this).execute()
        SaveStopData(context = this, listener = this).execute()
        SaveStopTimeData(context = this, listener = this).execute()

    }

    override fun onSaveRouteComplete() {
        Toast.makeText(this, "Routes data saved to DB successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveStopComplete() {
        Toast.makeText(this, "Stops data saved to DB successfully", Toast.LENGTH_SHORT).show()
        //TODO
    }

    override fun onSaveTripComplete() {
        Toast.makeText(this, "Trips data saved to DB successfully", Toast.LENGTH_SHORT).show()
        //TODO
    }

    override fun onSaveCalendarComplete() {
        Toast.makeText(this, "Calendar data saved to DB successfully", Toast.LENGTH_SHORT).show()
        //TODO
    }

    override fun onSaveStopTimeComplete() {
        Toast.makeText(this, "StopTimes data saved to DB successfully", Toast.LENGTH_SHORT).show()
        //TODO
    }
}
