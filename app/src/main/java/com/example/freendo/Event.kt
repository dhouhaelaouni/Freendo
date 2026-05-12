package com.example.freendo

data class Event(
    val id: String = "",
    val title: String = "",
    val dateTime: String = "",
    val location: String = "",
    val notes: String = "",
    val creatorId: String = "",
    val invitedFriendIds: List<String> = emptyList()
)