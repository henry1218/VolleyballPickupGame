package com.volleyball.pickup.game.models

sealed class FirestoreResult {
    data class Success(val operation: Operation) : FirestoreResult()
    data class Failure(val error: String) : FirestoreResult()
}

enum class Operation { ADD, UPDATE, DELETE, REGISTER, UNREGISTER }

