package com.volleyball.pickup.game

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class MainRepository @Inject constructor() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    fun signOut() {
        firebaseAuth.signOut()
    }
}