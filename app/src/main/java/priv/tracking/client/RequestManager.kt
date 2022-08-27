/*
 * Copyright 2015 - 2021 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("DEPRECATION")
package priv.tracking.client

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL

object RequestManager {

    private const val TIMEOUT = 15 * 1000
    private const val CHARSET = "utf-8"

    fun sendRequest(request: Request): Boolean {
        var inputStream: InputStream? = null
        return try {
            // Prepare URL
            val url = URL(request.url)

            // Make the connection
            val connection = url.openConnection() as HttpURLConnection
            connection.readTimeout = TIMEOUT
            connection.connectTimeout = TIMEOUT
            connection.requestMethod = "POST"

            // Add auth
            connection.setRequestProperty("Authorization", "Bearer "+request.secret)

            // Add multipart data
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            val writer = PrintWriter(OutputStreamWriter(connection.outputStream, CHARSET), true)
            request.writePositionToPrintWriter(writer)

            // Execute the request
            connection.connect()
            inputStream = connection.inputStream
            while (inputStream.read() != -1) {}
            true
        } catch (error: IOException) {
            false
        } finally {
            try {
                inputStream?.close()
            } catch (secondError: IOException) {
                Log.w(RequestManager::class.java.simpleName, secondError)
            }
        }
    }

    fun sendRequestAsync(request: Request, handler: RequestHandler) {
        var asynctask = RequestAsyncTask(handler)
        asynctask.execute(request)
    }

    interface RequestHandler {
        fun onComplete(success: Boolean)
    }

    private class RequestAsyncTask(private val handler: RequestHandler) : AsyncTask<Request, Unit, Boolean>() {

        override fun doInBackground(vararg request: Request): Boolean {
            return sendRequest(request[0])
        }

        override fun onPostExecute(result: Boolean) {
            handler.onComplete(result)
        }
    }
}
