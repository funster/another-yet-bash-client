package ru.aim.anotheryetbashclient.data.source.remote

const val ROOT_URL = "http://bash.im"
const val WITH_SLASH = "$ROOT_URL/"

fun wrapWithRoot(url: String = "") = WITH_SLASH + url

fun wrapWithRootWithoutSlash(url: String) = ROOT_URL + url

