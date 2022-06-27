package com.volleyball.pickup.game.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.volleyball.pickup.game.repo.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {
    val isUserAuthenticated: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun signInWithFacebook(accessToken: AccessToken) {
        repository.signInWithFacebook(accessToken, isUserAuthenticated)
    }
}