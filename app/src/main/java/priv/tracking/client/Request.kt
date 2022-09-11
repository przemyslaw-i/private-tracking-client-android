package priv.tracking.client

import android.content.SharedPreferences
import org.json.JSONObject
import java.io.PrintWriter

data class Request(
    val position: Position,
    val urlBase: String,
    val bib: String,
    val token: String,
) {
    fun writePositionToPrintWriter(writer: PrintWriter) {
        val json = JSONObject()
        json.put("bib", bib)
        json.put("ts", (position.time.time / 1000).toString())
        json.put("lat", position.latitude.toString())
        json.put("lon", position.longitude.toString())
        json.put("spd", position.speed.toString())
        json.put("alt", position.altitude.toString())
        json.put("batt", position.battery.toString())

        writer.append(json.toString())
        writer.flush()
    }
}