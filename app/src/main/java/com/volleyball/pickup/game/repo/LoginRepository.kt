package com.volleyball.pickup.game.repo

import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoginRepository @Inject constructor() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    fun signInWithFacebook(
        accessToken: AccessToken,
        isUserAuthenticated: MutableLiveData<Boolean>
    ) {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                isUserAuthenticated.value = firebaseAuth.currentUser != null
            } else {
                //TODO error handle
                isUserAuthenticated.value = false
            }
        }
    }
}