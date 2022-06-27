package com.volleyball.pickup.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.volleyball.pickup.game.models.Address
import com.volleyball.pickup.game.models.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private val _bottomNavVisible = MutableLiveData(true)
    val bottomNavVisible: LiveData<Boolean> = _bottomNavVisible

    fun setBottomNavVisible(visible: Boolean) {
        _bottomNavVisible.value = visible
    }

    fun getAddress(): Address {
        return repository.address
    }

    fun addPost(post: Post) {
        repository.addPost(post)
    }

    fun signOut() {
        repository.signOut()
    }
}