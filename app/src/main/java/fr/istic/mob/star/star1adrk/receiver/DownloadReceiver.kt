package fr.istic.mob.star.star1adrk.receiver

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import fr.istic.mob.star.star1adrk.R
import fr.istic.mob.star.star1adrk.database.AppDatabase
import fr.istic.mob.star.star1adrk.database.models.*
import fr.istic.mob.star.star1adrk.database.models.Calendar
import fr.istic.mob.star.star1adrk.service.DownloadService.Companion.getDownloadService
import fr.istic.mob.star.star1adrk.utils.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

private val TAG = "DownloadReceiver"

class DownloadReceiver : BroadcastReceiver() {

    companion object {
        private lateinit var zipFileName: String
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action

        if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.pref_key),
                Context.MODE_PRIVATE
            ) ?: return

            val downloadId = sharedPref.getLong(
                context.getString(R.string.download_id),
                0
            )
            val query = DownloadManager.Query()
            query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0))
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val cursor: Cursor = manager.query(query)
            if (cursor.moveToFirst()) {
                if (cursor.count > 0) {
                    val status: Int =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {


                        if (downloadId == intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0))
                            saveJsonInfoFileToDB(context)
                        else {
                            //get zip name from preference
                            Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                            unzipDatabase(zipFileName, context)
                        }
                    } else {
                        val message: Int =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                        Log.d(TAG, "onReceive: $message")
                        // So something here on failed.
                        if (downloadId != intent.getLongExtra(
                                DownloadManager.EXTRA_DOWNLOAD_ID,
                                0
                            )
                        ) {
                            //todo retry download after some delay
                        }
                    }
                }
            }
        }
    }

    private fun saveJsonInfoFileToDB(context: Context) {
        val db = AppDatabase.getInstance(context)
        val historyDao = db?.historyDao()
        val file = File(
            context.getExternalFilesDir(null)?.path, "/tco-busmetro-horaires-gtfs-versions-td.json"
        )

        val fileReader = FileReader(file)
        val bufferedReader = BufferedReader(fileReader)
        val stringBuilder = StringBuilder()
        var line: String? = bufferedReader.readLine()
        while (line != null) {
            stringBuilder.append(line).append("\n")
            line = bufferedReader.readLine()
        }
        bufferedReader.close()

        val response = stringBuilder.toString()
        val histories = Gson().fromJson(response, Array<History>::class.java)

        val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)

        val todayDate = Date()
        //check if record id is not in the database if so download else show toast
        histories.forEach {
            val recordId = it.recordId
            val history = historyDao?.getHistoryById(recordId)
            if (history != null) {
                Toast.makeText(context, "Database already existed", Toast.LENGTH_SHORT).show()
            } else {
                historyDao?.insertVersion(History(null, it.dbInfo, recordId))
                if (todayDate.after(simpleFormat.parse(it.dbInfo.validityStart))
                    && todayDate.before(
                        simpleFormat.parse(it.dbInfo.validityEnd)
                    )
                ) {
                    val url = it.dbInfo.url
                    showNotification(context, url)
                    zipFileName = url.split("/")[url.split("/").size - 1]
                }
            }
        }
    }

    private fun unzipDatabase(filename: String, context: Context) {
        try {
            val file = File(context.getExternalFilesDir(null)?.path, ZIP_FOLDER)
            if (!file.exists())
                file.mkdir()

            val fileInputStream =
                FileInputStream(context.getExternalFilesDir(null)?.path + "/" + filename)
            val zipInputStream = ZipInputStream(fileInputStream)
            var zipEntry: ZipEntry?
            while ((zipInputStream.nextEntry.also { zipEntry = it }) != null) {
                Log.d("Decompress", "Unzipping " + zipEntry?.name)
                val outputStream =
                    FileOutputStream(file.path + "/" + zipEntry?.name)
                val bufferedOutputStream = BufferedOutputStream(outputStream)
                val buffer = ByteArray(1024)
                var read: Int
                while (zipInputStream.read(buffer).also { read = it } > 0) {
                    bufferedOutputStream.write(buffer, 0, read)
                }
                bufferedOutputStream.close()
                zipInputStream.closeEntry()
                outputStream.close()
            }
            zipInputStream.closeEntry()
            zipInputStream.close()
            fileInputStream.close()

            saveTxtFilesToDB(context)
        } catch (e: Exception) {
            Log.e("Decompress", "unzip", e)
            Log.d("Unzip", "Unzipping failed")

            saveTxtFilesToDB(context)
        }
    }

    private fun saveTxtFilesToDB(context: Context) {
        //Clear database previous content
        val db = AppDatabase.getInstance(context)
        db?.routeDao()?.deleteAll()
        db?.stopDao()?.deleteAll()
        db?.tripDao()?.deleteAll()
        db?.stopTimeDao()?.deleteAll()
        db?.calendarDao()?.deleteAll()
        //test
        saveStopTimeData(context)

        saveCalendarData(context)
        saveRouteData(context)
        //apres quelques temps afficher la liste des routes

        saveTripData(context)
        saveStopData(context)
        saveStopTimeData(context)
    }

    private fun saveRouteData(context: Context) {
        val lines: List<String> = File(context.getExternalFilesDir(null)?.path, ROUTES).readLines()
        val routeDao = AppDatabase.getInstance(context)?.routeDao()
        for (i in 1 until lines.size) {
            val line = lines[i]
            val properties = line.split(",")
            routeDao?.insert(
                Route(
                    routeId = properties[0].replace("\"", ""),
                    routeShortName = properties[2].replace("\"", ""),
                    routeLongName = properties[3].replace("\"", ""),
                    routeDesc = properties[4].replace("\"", ""),
                    routeType = properties[5].replace("\"", ""),
                    routeColor = properties[7].replace("\"", ""),
                    routeTextColor = properties[8].replace("\"", ""),
                )
            )
        }
    }

    private fun saveTripData(context: Context) {
        val lines: List<String> = File(context.getExternalFilesDir(null)?.path, TRIPS).readLines()
        val tripDao = AppDatabase.getInstance(context)?.tripDao()
        for (i in 1 until lines.size) {
            val line = lines[i]
            val properties = line.split(",")
            properties.forEach {
                it.replace("\"", "")
            }
            tripDao?.insert(
                Trip(
                    routeId = properties[0].replace("\"", ""),
                    serviceId = properties[1].replace("\"", ""),
                    tripId = properties[2].replace("\"", "").replace("\"", ""),
                    tripHeadSign = properties[3].replace("\"", ""),
                    directionId = properties[5].replace("\"", ""),
                    blockId = properties[6].replace("\"", ""),
                    wheelchairAccessible = properties[8].replace("\"", ""),
                )
            )
        }
    }

    private fun saveStopData(context: Context) {
        val lines: List<String> = File(context.getExternalFilesDir(null)?.path, STOPS).readLines()
        val stopDao = AppDatabase.getInstance(context)?.stopDao()
        for (i in 1 until lines.size) {
            val line = lines[i]
            val properties = line.split(",")

            stopDao?.insert(
                Stop(
                    id = null,
                    stopId = properties[0].replace("\"", ""),
                    stopName = properties[2].replace("\"", ""),
                    stopDesc = properties[3].replace("\"", ""),
                    stopLat = properties[4].replace("\"", ""),
                    stopLon = properties[5].replace("\"", ""),
                    wheelchairBoarding = properties[11].replace("\"", ""),
                )
            )
        }
    }

    private fun saveStopTimeData(context: Context) {
        val lines: List<String> = File(context.getExternalFilesDir(null)?.path, STOP_TIMES).readLines()
        val stopTimeDao = AppDatabase.getInstance(context)?.stopTimeDao()
        for (i in 1 until lines.size) {
            val line = lines[i]
            val properties = line.split(",")
            stopTimeDao?.insert(
                StopTime(
                    id = null,
                    tripId = properties[0].replace("\"", ""),
                    arrivalTime = properties[1].replace("\"", ""),
                    departureTime = properties[2].replace("\"", ""),
                    stopId = properties[3].replace("\"", ""),
                    stopSequence = properties[4].replace("\"", ""),
                )
            )
        }
    }

    private fun saveCalendarData(context: Context) {
        val lines: List<String> = File(context.getExternalFilesDir(null)?.path, CALENDAR).readLines()
        val calendarDao = AppDatabase.getInstance(context)?.calendarDao()
        for (i in 1 until lines.size) {
            val line = lines[i]
            val properties = line.split(",")
            calendarDao?.insert(
                Calendar(
                    serviceId = properties[0].replace("\"", ""),
                    monday = properties[1].replace("\"", ""),
                    tuesday = properties[2].replace("\"", ""),
                    wednesday = properties[3].replace("\"", ""),
                    thursday = properties[4].replace("\"", ""),
                    friday = properties[5].replace("\"", ""),
                    saturday = properties[6].replace("\"", ""),
                    sunday = properties[7].replace("\"", ""),
                    startDate = properties[8].replace("\"", ""),
                    endDate = properties[8].replace("\"", ""),
                )
            )
        }
    }

    private fun showNotification(context: Context, downloadPath: String) {
        createNotificationChannel(context)
        val pendingIntent: PendingIntent = PendingIntent.getService(
            context, 0, getDownloadService(context, downloadPath),
            Intent.FILL_IN_DATA
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Star App")
            .setContentText("Click notification to download database data")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Star App"
            val descriptionText = "Click notification to download database data"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private val CHANNEL_ID = "star_app"
    private val notificationId = 121
}