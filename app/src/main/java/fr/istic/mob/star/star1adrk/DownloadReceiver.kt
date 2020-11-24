package fr.istic.mob.star.star1adrk

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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.google.gson.Gson
import fr.istic.mob.star.star1adrk.DownloadService.Companion.DOWNLOAD_PATH
import fr.istic.mob.star.star1adrk.DownloadService.Companion.getDownloadService
import fr.istic.mob.star.star1adrk.database.AppDatabase
import fr.istic.mob.star.star1adrk.database.Version
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.*


class DownloadReceiver : BroadcastReceiver() {
    private val TAG = "DownloadReceiver"

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action

        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "star_db"
        ).build()

        if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val query = DownloadManager.Query()
            query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0))
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val cursor: Cursor = manager.query(query)
            if (cursor.moveToFirst()) {
                if (cursor.count > 0) {
                    val status: Int =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        val sharedPref = context.getSharedPreferences(
                            context.getString(R.string.pref_key),
                            Context.MODE_PRIVATE
                        ) ?: return
                        val downloadId = sharedPref.getLong(
                            context.getString(R.string.download_id),
                            0
                        )

                        if (downloadId == intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0))
                            saveJsonInfoFileToDB(context)
                        else {
                            //unzip
                            //extract txt
                        }
                    } else {
                        val message: Int =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                        Log.d(TAG, "onReceive: $message")
                        // So something here on failed.
                    }
                }
            }
        }
    }

    private fun saveJsonInfoFileToDB(context: Context) {
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
        val versions = Gson().fromJson(response, Array<Version>::class.java)

        val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)

        val todayDate = Date()
        versions.forEach {
            if (todayDate.after(simpleFormat.parse(it.dbInfo.validityStart)) && todayDate.before(
                    simpleFormat.parse(it.dbInfo.validityEnd)
                )
            ) {
                showNotification(context, it.dbInfo.url)
            }
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