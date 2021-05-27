package com.hypnex.friengo.callbacks

interface AuthStateListener {
    fun onAuthChanged(isLoggedIn: Boolean)
}