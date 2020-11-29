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
import fr.istic.mob.star.star1adrk.R
import fr.istic.mob.star.star1adrk.database.AppDatabase
import fr.istic.mob.star.star1adrk.service.DownloadService.Companion.getDownloadService
import fr.istic.mob.star.star1adrk.utils.DOWNLOAD_FINISHED
import fr.istic.mob.star.star1adrk.utils.ObservableObject
import fr.istic.mob.star.star1adrk.utils.ZIP_FOLDER
import fr.istic.mob.star.star1adrk.utils.getHistoriesFromJson
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.concurrent.schedule

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
                            Timer("SettingUp", false).schedule(5000) {
                                //todo getDownloadService(context, downloadPath)
                                Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveJsonInfoFileToDB(context: Context) {
        val histories = getHistoriesFromJson(context)

        val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)

        val todayDate = simpleFormat.parse(simpleFormat.format(Date())) ?: return
        //check if record id is not in the database if so download else show toast
        histories?.forEach {
            val recordId = it.recordId
            val db = AppDatabase.getInstance(context)
            val historyDao = db?.historyDao()
            val history = historyDao?.getHistoryById(recordId)
            if (history != null) {
                Toast.makeText(context, "Database already existed", Toast.LENGTH_SHORT).show()
            } else {
                if (todayDate >= simpleFormat.parse(it.dbInfo.validityStart)
                    && todayDate <=
                    simpleFormat.parse(it.dbInfo.validityEnd)

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

            ObservableObject.instance.updateValue(DOWNLOAD_FINISHED)
        } catch (e: Exception) {
            Log.e("Decompress", "unzip", e)
            Log.d("Unzip", "Unzipping failed")

            ObservableObject.instance.updateValue(DOWNLOAD_FINISHED)
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