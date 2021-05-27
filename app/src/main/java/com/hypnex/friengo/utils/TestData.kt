package com.hypnex.friengo.utils

import com.hypnex.friengo.data.models.User

object UserListTestData {
    fun get() =
        listOf(
            User(
                displayName = "Вероника Жилина"
            ),
            User(
                displayName = "Сергей Сергеев"
            )
        )

    fun getChat() =
        listOf(
            User(
                displayName = "Сергей Фролов"
            ),
            User(
                displayName = "Алексей Давлатов"
            ),
            User(
                displayName = "Александр Громов"
            )
        )
}
