package com.volleyball.pickup.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.volleyball.pickup.game.models.Address
import com.volleyball.pickup.game.models.FirestoreResult
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

    private val _signedUpEventList = MutableLiveData<List<Post>>()
    val signedUpEventList: LiveData<List<Post>> = _signedUpEventList

    private val _postDetail = SingleLiveEvent<Post>()
    val postDetail: SingleLiveEvent<Post> = _postDetail

    private val _firestoreResult = MutableLiveData<FirestoreResult>()
    val firestoreResult: LiveData<FirestoreResult> = _firestoreResult

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

    fun fetchSignedUpEvent() {
        if (_signedUpEventList.value == null) {
            repository.fetchSignedUpEvents(_signedUpEventList)
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
        repository.addPost(post, _firestoreResult)
    }

    fun updatePost(post: Post) {
        post.postId = getTempPostForEdit().postId
        repository.updatePost(post, _firestoreResult)
    }

    fun deletePost(postId: String) {
        repository.deletePost(postId, _firestoreResult)
    }

    fun getTempPostForEdit(): Post = repository.getTempPost()

    fun setTempPostForEdit(post: Post) {
        repository.setTempPost(post)
    }

    fun signOut() {
        repository.signOut()
    }

    fun removeRegistration() {
        repository.removeRegistration()
    }

    fun updateEventStatus() {
        postDetail.value?.let {
            repository.updateEventState(it, _firestoreResult)
        }
    }

    fun updateFirestoreResult(result: FirestoreResult) {
        _firestoreResult.value = result
    }

    fun getUid(): String {
        return repository.getUid()
    }
}