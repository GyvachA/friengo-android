package com.hypnex.friengo.data.models

data class User(
    var id: String? = null,

    var displayName: String? = null,
    var username: String? = null,
    var photoUrl: String? = null,
    var email: String? = null,

    var longitude: Double? = null, // Долгота
    var latitude: Double? = null // Широта
) {
    companion object {
        const val USER_COLLECTION = "users"

        const val ID_COLUMN = "id"

        const val DISPLAY_NAME_COLUMN = "displayName"
        const val USERNAME_COLUMN = "username"
        const val PHOTO_URL_COLUMN = "photoUrl"
        const val EMAIL_COLUMN = "email"

        const val LONGITUDE_COLUMN = "longitude"
        const val LATITUDE_COLUMN = "latitude"
    }
}