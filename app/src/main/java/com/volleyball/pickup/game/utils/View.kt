package com.volleyball.pickup.game.utils

import android.view.View

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.visibleIf(predicate: Boolean) {
    this.visibility = if (predicate) View.VISIBLE else View.GONE
}