package com.hypnex.friengo.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hypnex.friengo.data.models.FriendsGroup
import com.hypnex.friengo.data.models.User
import com.hypnex.friengo.utils.Exceptions
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers

class FriendsGroupRepository {

    private val database = FirebaseFirestore
        .getInstance()

    fun initDefaultGroup(userEmail: String): Completable {
        return Completable.create { emitter ->

            val ref = database.collection(User.USER_COLLECTION)
                .document(userEmail)
                .collection(FriendsGroup.FRIENDS_GROUP_COLLECTION)
                .document(FriendsGroup.DEFAULT_GROUP_NAME)

            ref.get().addOnCompleteListener { task ->
                val document = task.result

                if (task.isSuccessful) {
                    if (document != null) {
                        if (!document.exists()) {
                            ref.set(
                                FriendsGroup(
                                    FriendsGroup.DEFAULT_GROUP_NAME,
                                    arrayListOf(),
                                    System.currentTimeMillis()
                                )
                            )
                                .addOnSuccessListener { emitter.onComplete() }
                                .addOnFailureListener { emitter.onError(it) }
                        }
                    }
                } else {
                    emitter.onError(task.exception!!)
                }
            }
        }.subscribeOn(Schedulers.io())
    }

    fun addFriend(userEmail: String, friendEmail: String): Completable {
        return Completable.create { emitter ->

            if (userEmail == friendEmail) {
                emitter.onError(Exceptions.tryToAddYourSelf)
                return@create
            }

            val checkRef = database.collection(User.USER_COLLECTION)
                .document(friendEmail)

            checkRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result

                    if (document != null) {
                        if (document.exists()) {
                            val userFriendsRef = database.collection(User.USER_COLLECTION)
                                .document(userEmail)
                                .collection(FriendsGroup.FRIENDS_GROUP_COLLECTION)
                                .document(FriendsGroup.DEFAULT_GROUP_NAME)
                            val friendFriendsRef = database.collection(User.USER_COLLECTION)
                                .document(friendEmail)
                                .collection(FriendsGroup.FRIENDS_GROUP_COLLECTION)
                                .document(FriendsGroup.DEFAULT_GROUP_NAME)
                            database.runBatch {
                                it.update(
                                    userFriendsRef,
                                    FriendsGroup.USER_IDS_COLUMN,
                                    FieldValue.arrayUnion(friendEmail)
                                )
                                it.update(
                                    friendFriendsRef,
                                    FriendsGroup.USER_IDS_COLUMN,
                                    FieldValue.arrayUnion(userEmail)
                                )
                            }
                                .addOnSuccessListener { emitter.onComplete() }
                                .addOnFailureListener { emitter.onError(it) }
                        } else {
                            emitter.onError(Exceptions.userNotExistsException)
                        }
                    }
                } else {
                    emitter.onError(task.exception!!)
                }
            }

        }.subscribeOn(Schedulers.io())
    }

    fun getFriendsGroups(userEmail: String): Observable<List<FriendsGroup>> {
        return Observable.create(ObservableOnSubscribe<List<FriendsGroup>> { emitter ->
            database.collection(User.USER_COLLECTION)
                .document(userEmail)
                .collection(FriendsGroup.FRIENDS_GROUP_COLLECTION)
                .orderBy(FriendsGroup.CREATED_TIME_COLUMN, Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        emitter.onError(error)
                        return@addSnapshotListener
                    }

                    val groups = arrayListOf<FriendsGroup>()

                    snapshot?.let {
                        for (doc in snapshot) {

                            groups.add(
                                FriendsGroup(
                                    doc.getString(FriendsGroup.TITLE_COLUMN),
                                    doc.get(FriendsGroup.USER_IDS_COLUMN) as List<String>,
                                    doc.getLong(FriendsGroup.CREATED_TIME_COLUMN),
                                )
                            )
                        }
                    }

                    emitter.onNext(groups)
                }
        }).subscribeOn(Schedulers.io())
    }
}