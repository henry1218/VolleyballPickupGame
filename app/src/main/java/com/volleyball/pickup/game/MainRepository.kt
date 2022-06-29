package com.volleyball.pickup.game

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.volleyball.pickup.game.models.Address
import com.volleyball.pickup.game.models.Post
import com.volleyball.pickup.game.utils.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

class MainRepository @Inject constructor() {
    var address = Address()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var firestore: FirebaseFirestore

    private lateinit var postDetailRegistration: ListenerRegistration

    private val postRef by lazy { firestore.collection("posts") }

    fun fetchPosts(postsResp: MutableLiveData<List<Post>>) {
        postRef.orderBy(
            "timestamp",
            Query.Direction.ASCENDING
        ).addSnapshotListener { snapshots, e ->
            if (e != null) {
                Timber.d("add snapshot error(${e.message}")
                return@addSnapshotListener
            }

            if (snapshots == null) {
                Timber.d("snapshots is null")
                return@addSnapshotListener
            }

            val list = snapshots.documents.mapNotNull { it.toObject<Post>() }
            postsResp.postValue(list)
        }
    }

    fun fetchHostEvents(hostEventResp: MutableLiveData<List<Post>>) {
        postRef.whereEqualTo("hostUid", firebaseAuth.uid).orderBy(
            "timestamp",
            Query.Direction.ASCENDING
        ).addSnapshotListener { snapshots, e ->
            if (e != null) {
                Timber.d("add snapshot error(${e.message})")
                return@addSnapshotListener
            }

            if (snapshots == null) {
                Timber.d("snapshots is null")
                return@addSnapshotListener
            }

            val list = snapshots.documents.mapNotNull { it.toObject<Post>() }
            hostEventResp.postValue(list)
        }
    }

    fun fetchPostDetail(postId: String, postDetailResp: SingleLiveEvent<Post>) {
        postDetailRegistration = postRef.document(postId).addSnapshotListener { snapshots, e ->
            if (e != null) {
                Timber.d("add snapshot error(${e.message})")
                return@addSnapshotListener
            }

            if (snapshots == null) {
                Timber.d("snapshots is null")
                return@addSnapshotListener
            }

            postDetailResp.postValue(snapshots.toObject<Post>())
        }
    }

    fun addPost(post: Post) {
        post.postId = postRef.document().id
        post.hostUid = firebaseAuth.uid ?: ""
        postRef.document(post.postId).set(post).addOnSuccessListener {
            Timber.d("DocumentSnapshot added Success")
        }.addOnFailureListener { e ->
            Timber.w("Error adding document", e)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun removeRegistration() {
        postDetailRegistration.remove()
    }
}