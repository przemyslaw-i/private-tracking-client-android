package priv.tracking.client

import org.json.JSONObject
import java.io.PrintWriter

data class Request(
    val url: String?,
    val position: Position,
    val secret: String?
) {
    fun writePositionToPrintWriter(writer: PrintWriter) {
        val json = JSONObject()
        json.put("id", position.id.toString())
        json.put("ts", (position.time.time / 1000).toString())
        json.put("lat", position.latitude.toString())
        json.put("lon", position.longitude.toString())
        json.put("spd", position.speed.toString())
        json.put("ber", position.course.toString())
        json.put("alt", position.altitude.toString())
        json.put("acc", position.accuracy.toString())
        json.put("batt", position.battery.toString())

        if (position.charging) {
            json.put("chrg", position.charging.toString())
        }

        if (position.mock) {
            json.put("mock", position.mock.toString())
        }

        writer.append(json.toString())
        writer.flush()
    }
}