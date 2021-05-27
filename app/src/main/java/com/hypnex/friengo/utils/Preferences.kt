package com.hypnex.friengo.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import com.hypnex.friengo.data.models.User

class Preferences(private val sharedPreferences: SharedPreferences) {

    private val gson = Gson()

    fun saveUser(user: User) {
        val json = gson.toJson(user)
        sharedPreferences.edit()
            .putString(USER_AUTH, json)
            .apply()
    }

    fun getUser(): User {
        val json = sharedPreferences.getString(USER_AUTH, null)

        return gson.fromJson(json, User::class.java) ?: User()
    }

    fun removeUser() {
        sharedPreferences.edit()
            .remove(USER_AUTH)
            .apply()
    }

    companion object {
        const val USER_AUTH = "USER_AUTH"
        const val PREF_NAME = "AUTHENTICATION"
    }

}