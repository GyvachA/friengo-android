package com.hypnex.friengo.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hypnex.friengo.data.models.Event
import com.hypnex.friengo.data.models.User
import com.hypnex.friengo.utils.Exceptions
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers

class EventRepository {

    private val database = FirebaseFirestore
        .getInstance()
        .collection(User.USER_COLLECTION)

    fun createEvent(event: Event, user: User): Completable {
        return Completable.create { emitter ->

            database.document(user.email!!)
                .collection(Event.EVENT_COLLECTION)
                .add(event)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }

        }.subscribeOn(Schedulers.io())
    }

    fun getUserEvents(userEmail: String): Single<List<Event>> {
        return Single.create(SingleOnSubscribe<List<Event>> { emitter ->

            database.document(userEmail)
                .collection(Event.EVENT_COLLECTION)
                .get()
                .addOnCompleteListener {  task ->
                    val documents = task.result

                    if (documents != null) {

                        val events = arrayListOf<Event>()

                        for (doc in documents) {
                            events.add(doc.toObject(Event::class.java))
                        }

                        emitter.onSuccess(events)
                    } else {
                        emitter.onError(Exceptions.userNotExistsException)
                    }
                }
                .addOnFailureListener { emitter.onError(it) }

        }).subscribeOn(Schedulers.io())
    }

}