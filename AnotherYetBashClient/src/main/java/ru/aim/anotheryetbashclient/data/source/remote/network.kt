package ru.aim.anotheryetbashclient.data.source.remote

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import ru.aim.anotheryetbashclient.BuildConfig
import ru.aim.anotheryetbashclient.helper.L
import java.io.InputStream

private const val TAG = "network"

private val networkClient = OkHttpClient()

val Response.encoding: String
    get() = this.header("Content-Type").split("=")[1]

val Response.data: InputStream
    get() = this.body().byteStream()

data class NetworkRequestResult(val stream: InputStream, val url: String, val encoding: String)

interface NetworkClient {

    fun getNetworkRequest(url: String): NetworkRequestResult
}

class SimpleNetworkClient : NetworkClient {

    override fun getNetworkRequest(url: String): NetworkRequestResult {
        val builder = Request.Builder()
        builder.get().url(url)
        if (BuildConfig.DEBUG) {
            L.d(TAG, "Get request: $url")
        }
        val response = networkClient.newCall(builder.build()).execute()
        return NetworkRequestResult(response.data, url, response.encoding)
    }
}