package com.volleyball.pickup.game

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SplashRepository @Inject constructor() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    fun checkIfUserIsAuthenticated(): MutableLiveData<Boolean> {
        return if (firebaseAuth.currentUser == null) {
            MutableLiveData(false)
        } else {
            MutableLiveData(true)
        }
    }
}