package com.hypnex.friengo.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Event(
    var longitude: Double? = null, // Долгота
    var latitude: Double? = null, // Широта

    var title: String? = null,
    var description: String? = null,

    var photoPath: String? = null,

    var deadlineDate: Date? = null
): Parcelable {
    companion object {
        const val EVENT_COLLECTION = "events"

        const val LONGITUDE_COLUMN = "longitude"
        const val LATITUDE_COLUMN = "latitude"

        const val TITLE_COLUMN = "title"
        const val DESCRIPTION_COLUMN = "description"

        const val PHOTO_PATH_COLUMN = "photoPath"

        const val DEADLINE_DATE_COLUMN = "deadlineDate"
    }
}