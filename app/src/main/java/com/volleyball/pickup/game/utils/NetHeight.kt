package com.volleyball.pickup.game.utils

import com.volleyball.pickup.game.R

enum class NetHeight(val id: Int) {
    MAN(0),
    WOMAN(1);

    companion object {
        fun getStringRes(id: Int): Int {
            return when (id) {
                MAN.id -> R.string.net_height_man
                WOMAN.id -> R.string.net_height_woman
                else -> {
                    throw Exception("Wrong NetHeight id($id)")
                }
            }
        }
    }
}