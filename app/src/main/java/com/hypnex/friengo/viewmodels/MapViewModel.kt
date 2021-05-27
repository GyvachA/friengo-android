package com.hypnex.friengo.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hypnex.friengo.data.models.Event
import com.hypnex.friengo.data.repository.EventRepository
import com.hypnex.friengo.data.repository.FriendsGroupRepository
import com.hypnex.friengo.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

class MapViewModel : BaseViewModel() {

    private val eventRepository = EventRepository()

    private val friendsGroupRepository = FriendsGroupRepository()

    var eventsInformation = arrayListOf<Event>()

    val friendEmails = MutableLiveData<List<String>>()

    fun getUserEvent(email: String, onSuccess: (events: List<Event>) -> Unit) {
        isLoad.value = View.VISIBLE

        disposable.add(
            eventRepository.getUserEvents(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        isLoad.value = View.GONE
                        eventsInformation.addAll(it)
                        onSuccess(it)
                    },
                    {
                        isLoad.value = View.GONE
                        toastMessage.value = "Ошибка загрузки"
                    }
                )
        )
    }

    fun getDefaultGroupEmails(userEmail: String) {

        isLoad.value = View.VISIBLE

        disposable.add(
            friendsGroupRepository.getFriendsGroups(userEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        isLoad.value = View.GONE
                        friendEmails.value = it[0].userIds!!
                    },
                    {
                        isLoad.value = View.GONE
                        toastMessage.value = "Ошибка загрузки"
                        it.printStackTrace()
                    }
                )
        )
    }

}