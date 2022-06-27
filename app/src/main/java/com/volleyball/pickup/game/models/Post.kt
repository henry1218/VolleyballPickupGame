package com.volleyball.pickup.game.models

import com.google.firebase.Timestamp

data class Post(
    var postId: String = "",
    val title: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val city: String = "",
    val locality: String = "",
    val location: String = "",
    val netHeight: Int = 0,
    val fee: Int = 0,
    val needMen: Int = 0,
    val needWomen: Int = 0,
    val needBoth: Int = 0,
    val additionalInfo: String = "",
    val profilePic: String = "",
    val players: List<String> = listOf()
)
