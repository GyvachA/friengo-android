package com.hypnex.friengo.data.models

data class FriendsGroup(
    var title: String? = null,
    var userIds: List<String>? = null,

    var createdTime: Long? = null
) {
    companion object {
        const val FRIENDS_GROUP_COLLECTION = "friendsGroup"

        const val TITLE_COLUMN = "title"
        const val USER_IDS_COLUMN = "userIds"

        const val CREATED_TIME_COLUMN = "createdTime"

        const val DEFAULT_GROUP_NAME = "Все" // Main group
    }
}
