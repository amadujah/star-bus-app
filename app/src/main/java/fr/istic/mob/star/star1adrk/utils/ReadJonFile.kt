package fr.istic.mob.star.star1adrk.utils

import android.content.Context
import com.google.gson.Gson
import fr.istic.mob.star.star1adrk.database.models.History
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

fun getHistoriesFromJson(context: Context): Array<History>? {
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
    return histories
}