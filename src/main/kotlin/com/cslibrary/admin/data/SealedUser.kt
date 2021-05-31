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
) {
    override fun toString(): String {
        return """
            Information for user: $userName
            ID: $userId
            Name: $userName
            P.N: $userPhoneNumber
            Left Time: $leftTime
            Total Study Time: $totalStudyTime
            Current Seat Number: $reservedSeatNumber
            Current State: ${userState.name}
            Is user NOT banned? : $userNonBanned
        """.trimIndent()
    }
}