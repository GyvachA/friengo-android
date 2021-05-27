package com.hypnex.friengo.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hypnex.friengo.data.models.FriendsGroup
import com.hypnex.friengo.data.models.User
import com.hypnex.friengo.data.repository.FriendsGroupRepository
import com.hypnex.friengo.data.repository.UserRepository
import com.hypnex.friengo.utils.BaseViewModel
import com.hypnex.friengo.utils.Exceptions
import io.reactivex.android.schedulers.AndroidSchedulers

class FriendsViewModel : BaseViewModel() {

    private val friendsGroupRepository = FriendsGroupRepository()

    private val userRepository = UserRepository()

    val groupsInformation = MutableLiveData<List<FriendsGroup>>()

    val usersList = MutableLiveData<List<User>>()

    fun addFriend(userEmail: String, friendEmail: String) {
        isLoad.value = View.VISIBLE

        disposable.add(
            friendsGroupRepository.addFriend(userEmail, friendEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        isLoad.value = View.GONE
                    },
                    {
                        isLoad.value = View.GONE
                        if (it == Exceptions.userNotExistsException) toastMessage.value = it.message
                        if (it == Exceptions.tryToAddYourSelf) toastMessage.value = it.message
                        it.printStackTrace()
                    }
                )
        )
    }

    fun loadUserFriendGroups(userEmail: String) {
        isLoad.value = View.VISIBLE

        disposable.add(
            friendsGroupRepository.getFriendsGroups(userEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        isLoad.value = View.GONE
                        groupsInformation.value = it!!
                        Log.d("WARNING", "$it")
                    },
                    {
                        isLoad.value = View.GONE
                        toastMessage.value = "Проблемы с соединением"
                        it.printStackTrace()
                    }
                )
        )
    }

    fun loadUsersList(emails: List<String>) {
        isLoad.value = View.VISIBLE

        disposable.add(
            userRepository.getUsersByEmails(emails)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        isLoad.value = View.GONE
                        usersList.value = it
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