package com.cslibrary.admin.data

data class NotifyUserRequest(
    var userId: String,
    var userNotification: UserNotification
)

data class UserNotification(
    var notificationTitle: String,
    var notificationMessage: String
)