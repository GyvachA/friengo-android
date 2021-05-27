package com.hypnex.friengo.utils

class Exceptions {

    companion object {
        val userNotExistsException = Exception("Пользователь не существует")
        val tryToAddYourSelf = Exception("Невозможно добавить себя в друзья")
    }
}