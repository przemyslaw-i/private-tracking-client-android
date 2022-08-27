package priv.tracking.client

import java.io.PrintWriter

data class Request(
    val url: String?,
    val position: Position,
    val secret: String?
) {
    fun writePositionToPrintWriter(writer: PrintWriter) {
        val id = position.id.toString()
        val timestamp = (position.time.time / 1000).toString()
        val lat = position.latitude.toString()
        val lon =  position.longitude.toString()
        val speed = position.speed.toString()
        val bearing = position.course.toString()
        val altitude = position.altitude.toString()
        val accuracy = position.accuracy.toString()
        val batt = position.battery.toString()

        var params = "id=$id&ts=$timestamp&lat=$lat&lon=$lon&spd=$speed&ber=$bearing&alt=$altitude&acc=$accuracy&batt=$batt"

        if (position.charging) {
            val charge = position.charging.toString()
            params += "&chrg=$charge"
        }

        if (position.mock) {
            val mock = position.mock.toString()
            params += "&mock=$mock"
        }

        writer.append(params)
        writer.flush()
    }
}