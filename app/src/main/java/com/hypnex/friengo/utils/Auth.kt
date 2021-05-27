package com.hypnex.friengo.utils

import com.hypnex.friengo.callbacks.AuthStateListener

abstract class Auth<Input, Result> {

    protected val callbacks = mutableListOf<AuthStateListener>()

    abstract fun isLoggedIn(): Boolean

    abstract fun startSignIn(authInfo: Input? = null)

    abstract fun handleSignInResult(result: Result?, onSuccess: () -> Unit, onError: () -> Unit)

    fun addAuthChangeListener(listener: AuthStateListener) {
        callbacks.add(listener)
    }

    fun removeAuthChangeListener(listener: AuthStateListener) {
        callbacks.remove(listener)
    }

    open fun clear() {
        callbacks.clear()
    }

    abstract fun signOut()

}