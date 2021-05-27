package com.hypnex.friengo.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hypnex.friengo.data.models.User
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers

class UserRepository {

    private val database = FirebaseFirestore
        .getInstance()
        .collection(User.USER_COLLECTION)

    fun create(user: User): Single<User> {
        return Single.create(SingleOnSubscribe<User> { emitter ->
            val ref = database.document(user.email!!)

            ref.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result

                    if (document != null) {
                        if (document.exists()) {
                            emitter.onError(Exception("User exists"))
                        } else {
                            ref.set(user)
                                .addOnSuccessListener { emitter.onSuccess(user) }
                                .addOnFailureListener { emitter.onError(it) }
                        }
                    }
                } else {
                    emitter.onError(task.exception!!)
                }
            }
        }).subscribeOn(Schedulers.io())
    }

    fun getByEmail(email: String): Single<User> {
        return Single.create(SingleOnSubscribe<User> { emitter ->
            val ref = database.document(email)

            ref.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result

                    if (document != null) {
                        if (document.exists()) {
                            val user = document.toObject(User::class.java)

                            emitter.onSuccess(user!!)
                        } else {
                            emitter.onError(Exception("User not exists"))
                        }
                    }
                } else {
                    emitter.onError(task.exception!!)
                }
            }
        }).subscribeOn(Schedulers.io())
    }

    fun getUsersByEmails(emails: List<String>): Single<List<User>> {
        return Single.create(SingleOnSubscribe<List<User>> { emitter ->
            database.whereIn(User.EMAIL_COLUMN, emails)
                .get()
                .addOnCompleteListener { task ->
                    val listUser = arrayListOf<User>()

                    if (task.isSuccessful) {
                        val documents = task.result
                        if (documents != null) {
                            for (doc in documents) {
                                listUser.add(doc.toObject(User::class.java))
                            }
                            emitter.onSuccess(listUser)
                        }
                    } else {
                        emitter.onError(task.exception!!)
                    }

                }
        }).subscribeOn(Schedulers.io())
    }

}