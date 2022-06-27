package com.volleyball.pickup.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: SplashRepository) : ViewModel() {
    lateinit var isUserAuthenticated: MutableLiveData<Boolean>

    fun checkIfUserIsAuthenticated() {
        isUserAuthenticated = repository.checkIfUserIsAuthenticated()
    }
}