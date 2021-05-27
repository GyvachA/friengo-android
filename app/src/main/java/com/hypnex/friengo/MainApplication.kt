package com.hypnex.friengo

import android.app.Application
import android.content.Context
import com.hypnex.friengo.utils.Preferences

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        preferences = Preferences(getSharedPreferences(Preferences.PREF_NAME, Context.MODE_PRIVATE))
    }

    companion object {
        lateinit var preferences: Preferences private set
    }
}