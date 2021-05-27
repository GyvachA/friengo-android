package com.hypnex.friengo.utils

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    protected val disposable = CompositeDisposable()

    val isLoad = MutableLiveData(View.GONE)

    val toastMessage = MutableLiveData<String>()

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

}