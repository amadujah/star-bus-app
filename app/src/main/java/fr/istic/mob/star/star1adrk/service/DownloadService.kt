package fr.istic.mob.star.star1adrk.service

import android.app.DownloadManager
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.Uri
import fr.istic.mob.star.star1adrk.R
import java.io.File


class DownloadService : IntentService("DownloadingService") {

    override fun onHandleIntent(intent: Intent?) {
        val downloadPath = intent?.getStringExtra(DOWNLOAD_PATH)

        if (downloadPath != null)
            startDownload(downloadPath, this)
    }

    companion object {
        val DOWNLOAD_PATH =
            "fr.istic.star_DownloadSongService_Download_path"

        fun getDownloadService(
            callingClassContext: Context,
            downloadPath: String
        ): Intent {
            return Intent(callingClassContext, DownloadService::class.java)
                .putExtra(DOWNLOAD_PATH, downloadPath)
        }
    }

    private fun startDownload(downloadPath: String, context: Context) {
        val uri = Uri.parse(downloadPath)
        var downloadTitle = ""

        val isZipDownload: Boolean = downloadPath.endsWith(".zip")
        if (!isZipDownload) {
            downloadTitle = "Downloading json file"
            val file = File(
                context.getExternalFilesDir(null)?.path,
                "/tco-busmetro-horaires-gtfs-versions-td.json"
            )

            if (file.exists())
                file.delete()

        } else {
            downloadTitle = "Downloading zip file"
            val file = File(
                context.getExternalFilesDir(null)?.path,
                uri.lastPathSegment!!
            )
            if (file.exists())
                file.delete()
        }


        val request = DownloadManager.Request(uri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI) // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // This will show notification on top when downloading the file.
        request.setTitle(downloadTitle) // Title for notification.
        request.addRequestHeader("Content-type", "application/octet-stream")
        request.setMimeType("application/octet-stream")
        val downloadPathArray = downloadPath.split("/")
        val path = if (uri.lastPathSegment != null && uri.lastPathSegment!!.endsWith(".zip"))
            uri.lastPathSegment
        else
            downloadPathArray[5] + ".json"
        request.setDestinationInExternalFilesDir(
            context, null,
            path
        ) // Storage directory path
        val id =
            (getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request) // This will start downloading
        //store this id in sharedpreferences
        if (!isZipDownload) {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.pref_key),
                Context.MODE_PRIVATE
            ) ?: return

            with(sharedPref.edit()) {
                putLong(getString(R.string.download_id), id)
                apply()
            }
        }
    }
}