package ru.aim.anotheryetbashclient.data.remote

import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.InputStream

val networkClient = OkHttpClient()

internal const val ROOT_URL = "http://bash.im"
internal const val WITH_SLASH = "$ROOT_URL/"

fun wrapWithRoot(url: String = "") = WITH_SLASH + url

fun wrapWithRootWithoutSlash(url: String) = ROOT_URL + url

val Response.encoding: String
    get() = this.header("Content-Type").split("=")[1]

val Response.data: InputStream
    get() = this.body().byteStream()

class NetworkRequestResult(val stream: InputStream, val url: String, val encoding: String)