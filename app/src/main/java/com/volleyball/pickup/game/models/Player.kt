package com.volleyball.pickup.game.models

import com.google.firebase.Timestamp

data class Player(
    val name: String = "",
    val uid: String = "",
    val profilePic: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
