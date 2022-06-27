package com.volleyball.pickup.game.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.volleyball.pickup.game.repo.SplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: SplashRepository) : ViewModel() {
    lateinit var isUserAuthenticated: MutableLiveData<Boolean>

    fun checkIfUserIsAuthenticated() {
        isUserAuthenticated = repository.checkIfUserIsAuthenticated()
    }
}