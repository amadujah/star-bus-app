package fr.istic.mob.star.star1adrk

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import fr.istic.mob.star.star1adrk.database.AppDatabase
import fr.istic.mob.star.star1adrk.database.models.Route
import fr.istic.mob.star.star1adrk.service.DownloadService
import fr.istic.mob.star.star1adrk.task.*
import fr.istic.mob.star.star1adrk.utils.*
import java.util.*
import android.util.Log
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), Observer, SaveDataCallbacks, AdapterView.OnItemSelectedListener,
                     DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0


    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 100
    private var fileUrl =
        "https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/download/?format=json&timezone=Europe/Berlin&lang=fr"
    private lateinit var progressBar: ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // selectDate
        pickDate()

        // Spinner routes
        val db = AppDatabase.getInstance(this)
        val routes: List<Route> = db?.routeDao()?.loadAllRoutes()!!
        val spinner = findViewById<Spinner>(R.id.spinner2)
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            routes.map { it.routeShortName }
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
            spinner.onItemSelectedListener = this
        }

        // spinner trip
//        val spinnerTrip = findViewById<Spinner>(R.id.spinner_trip)
        /*spinnerTrip.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            routes.map { it.routeShortName }
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
            spinner.onItemSelectedListener = this
        }*/


        progressBar = findViewById(R.id.loadingPanel)
        progressBar.visibility = View.GONE

        ObservableObject.instance.addObserver(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasPermission =
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED)
            if (!hasPermission) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE
                )
            } else {
                startService(
                    DownloadService.getDownloadService(
                        this,
                        fileUrl
                    )
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startService(
                    DownloadService.getDownloadService(
                        this,
                        fileUrl
                    )
                )
        }
    }

    //Receiver finished unzipping files
    override fun update(o: Observable?, arg: Any?) {
        if (arg == DOWNLOAD_STARTED) {
            this.runOnUiThread {
                progressBar.visibility = View.VISIBLE
            }
        } else if (arg == DOWNLOAD_FINISHED) {
           //Clear database previous content
            val db = AppDatabase.getInstance(this)
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
    }

    override fun onSaveRouteComplete() {
        this.runOnUiThread {
            progressBar.visibility = View.GONE
        }
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

    // spinner
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        val text = parent.getItemAtPosition(pos).toString()
        Toast.makeText(parent.context, text, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    // select date
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year
        getDateTimeCalendar()
        TimePickerDialog(this, this, hour, minute, true).show()
    }

    // select time
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val datePicker = findViewById<TextView>(R.id.date_picker)
        val timePicker = findViewById<TextView>(R.id.time_picker)

        savedHour = hourOfDay
        savedMinute = minute
        datePicker.text = "$savedDay-$savedMonth-$savedYear"
        timePicker.text = "$savedHour:$savedMinute"
    }

    private fun getDateTimeCalendar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate(){
        val datePicker = findViewById<TextView>(R.id.date_picker)
        /*val textView: TextView = findViewById(R.id.date_picker) as TextView
        textView.setOnClickListener {
            textView.text = ""
        }*/
        datePicker.setOnClickListener {
            getDateTimeCalendar()

            DatePickerDialog(this, this, year, month, day).show()
        }
    }

    fun TextView.Spinner(color: String) = this.setTextColor(color.toInt())

}