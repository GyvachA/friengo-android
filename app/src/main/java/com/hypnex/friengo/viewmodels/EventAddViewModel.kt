package com.hypnex.friengo.viewmodels

import com.hypnex.friengo.data.models.Event
import com.hypnex.friengo.data.models.User
import com.hypnex.friengo.data.repository.EventRepository
import com.hypnex.friengo.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

class EventAddViewModel: BaseViewModel() {

    private val eventRepository = EventRepository()

    fun createEvent(event: Event, user: User) {
        disposable.add(
            eventRepository.createEvent(event, user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        toastMessage.value = "Событие успешно добавлено"
                    },
                    {
                        toastMessage.value = "Событие не было добавлено"
                        it.printStackTrace()
                    }
                )
        )
    }

}