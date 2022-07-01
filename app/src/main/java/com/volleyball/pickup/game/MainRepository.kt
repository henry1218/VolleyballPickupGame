package com.volleyball.pickup.game

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.volleyball.pickup.game.models.Address
import com.volleyball.pickup.game.models.Player
import com.volleyball.pickup.game.models.Post
import com.volleyball.pickup.game.utils.ProfileUtils
import com.volleyball.pickup.game.utils.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

class MainRepository @Inject constructor() {
    var address = Address()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var profileUtils: ProfileUtils

    private var tempPostForEdit = Post()

    private lateinit var postDetailRegistration: ListenerRegistration

    private val postRef by lazy { firestore.collection("posts") }

    fun fetchPosts(postsResp: MutableLiveData<List<Post>>) {
        postRef
            .whereGreaterThanOrEqualTo("endTimestamp", Timestamp.now())
            .orderBy("endTimestamp", Query.Direction.ASCENDING)
            .orderBy("startTimestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
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
        postRef.whereEqualTo("hostUid", firebaseAuth.uid)
            .whereGreaterThanOrEqualTo("endTimestamp", Timestamp.now())
            .orderBy("endTimestamp", Query.Direction.ASCENDING)
            .orderBy(
                "startTimestamp",
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

    fun fetchSignedUpEvents(signedUpEventResp: MutableLiveData<List<Post>>) {
        postRef.whereArrayContains("players", firebaseAuth.uid.toString())
            .whereGreaterThanOrEqualTo("endTimestamp", Timestamp.now())
            .orderBy("endTimestamp", Query.Direction.ASCENDING)
            .orderBy(
                "startTimestamp",
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
                signedUpEventResp.postValue(list)
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
        post.hostUid = firebaseAuth.uid.toString()
        postRef.document(post.postId).set(post).addOnSuccessListener {
            Timber.d("DocumentSnapshot added Success")
        }.addOnFailureListener { e ->
            Timber.w("Error adding document", e)
        }
    }

    fun updatePost(post: Post) {
        post.hostUid = firebaseAuth.uid.toString()
        postRef.document(post.postId)
            .update(
                mapOf(
                    "title" to post.title,
                    "date" to post.date,
                    "startTime" to post.startTime,
                    "endTime" to post.endTime,
                    "startTimestamp" to post.startTimestamp,
                    "endTimestamp" to post.endTimestamp,
                    "city" to post.city,
                    "locality" to post.locality,
                    "location" to post.location,
                    "netHeight" to post.netHeight,
                    "fee" to post.fee,
                    "needMen" to post.needMen,
                    "needWomen" to post.needWomen,
                    "needBoth" to post.needBoth,
                    "additionalInfo" to post.additionalInfo,
                    "additionalInfo" to post.additionalInfo,
                )
            )
            .addOnCanceledListener {
                //TODO show hint
            }
    }

    fun deletePost(postId: String) {
        postRef.document(postId)
            .delete()
            .addOnCanceledListener {
                //TODO show hint
            }
    }

    fun getTempPost(): Post = tempPostForEdit

    fun setTempPost(post: Post) {
        tempPostForEdit = post
    }

    fun updateEventState(post: Post) {
        val playersDocument = postRef.document(post.postId)
            .collection("players")
            .document(firebaseAuth.uid.toString())
        if (post.players.contains(firebaseAuth.uid.toString())) {
            playersDocument.delete()
                .addOnSuccessListener {
                    postRef.document(post.postId)
                        .update("players", FieldValue.arrayRemove(firebaseAuth.uid.toString()))
                        .addOnSuccessListener {
                            //TODO show hint
                        }
                }
        } else {
            playersDocument.set(
                Player(
                    name = profileUtils.getName(),
                    uid = firebaseAuth.uid.toString(),
                    profilePic = profileUtils.getSmallProfilePic(),
                    timestamp = Timestamp.now()
                )
            ).addOnSuccessListener {
                postRef.document(post.postId)
                    .update(
                        "players", FieldValue.arrayUnion(firebaseAuth.uid.toString())
                    ).addOnSuccessListener() {
                        //TODO show hint
                    }
            }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun removeRegistration() {
        postDetailRegistration.remove()
    }

    fun getUid(): String {
        return firebaseAuth.uid.toString()
    }
}