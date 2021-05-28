package com.cslibrary.admin.data

enum class UserState {
    BREAK, EXIT, START
}

data class SealedUser(
    var userId: String,
    var userName: String,
    var userPhoneNumber: String,
    var leftTime: Long,
    var totalStudyTime: Long,
    var reservedSeatNumber: String,
    var userState: UserState,
    var userNonBanned: Boolean
)