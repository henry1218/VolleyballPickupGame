package com.volleyball.pickup.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.volleyball.pickup.game.models.Address
import com.volleyball.pickup.game.models.Post
import com.volleyball.pickup.game.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private val _bottomNavVisible = MutableLiveData(true)
    val bottomNavVisible: LiveData<Boolean> = _bottomNavVisible

    private val _postList = MutableLiveData<List<Post>>()
    val postList: LiveData<List<Post>> = _postList

    private val _hostEventList = MutableLiveData<List<Post>>()
    val hostEventList: LiveData<List<Post>> = _hostEventList

    private val _postDetail = SingleLiveEvent<Post>()
    val postDetail: SingleLiveEvent<Post> = _postDetail

    fun fetchPosts() {
        if (_postList.value == null) {
            repository.fetchPosts(_postList)
        }
    }

    fun fetchHostEvent() {
        if (_hostEventList.value == null) {
            repository.fetchHostEvents(_hostEventList)
        }
    }

    fun fetchPostDetail(postId: String) {
        repository.fetchPostDetail(postId, _postDetail)
    }

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

    fun removeRegistration() {
        repository.removeRegistration()
    }

    fun updateEventStatus() {
        postDetail.value?.let {
            repository.updateEventState(it)
        }
    }

    fun getUid(): String {
        return repository.getUid()
    }
}