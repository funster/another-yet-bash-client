package ru.aim.anotheryetbashclient.data

data class Quote(
        var id: Long? = null,
        var text: String? = null,
        var publicId: String? = null,
        var rating: String? = null,
        var date: String? = null,
        var isNew: Boolean? = null,
        var flag: Int? = null
)