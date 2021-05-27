package com.hypnex.friengo.viewmodels

import android.view.View
import com.hypnex.friengo.data.models.User
import com.hypnex.friengo.data.repository.FriendsGroupRepository
import com.hypnex.friengo.data.repository.UserRepository
import com.hypnex.friengo.utils.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

class MainActivityViewModel : BaseViewModel() {

    private val userRepository = UserRepository()

    private val friendsGroupRepository = FriendsGroupRepository()

    fun createUser(user: User, actionOnSuccess: () -> Unit, actionOnError: () -> Unit) {
        isLoad.value = View.VISIBLE

        disposable.add(
            userRepository.create(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        friendsGroupRepository.initDefaultGroup(it.email!!)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                {
                                    isLoad.value = View.GONE
                                    actionOnSuccess()
                                },
                                {
                                    isLoad.value = View.GONE
                                    actionOnError()
                                }
                            )
                    },
                    {
                        isLoad.value = View.GONE
                        actionOnError()
                    }
                )
        )
    }

}