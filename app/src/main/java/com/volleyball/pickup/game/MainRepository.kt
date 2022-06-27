package com.volleyball.pickup.game

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.volleyball.pickup.game.models.Address
import com.volleyball.pickup.game.models.Post
import timber.log.Timber
import javax.inject.Inject

class MainRepository @Inject constructor() {

    var address = Address()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var firestore: FirebaseFirestore

    private val postRef by lazy { firestore.collection("posts") }

    fun addPost(post: Post) {
        post.postId = postRef.document().id
        postRef.document(post.postId).set(post).addOnSuccessListener {
            Timber.d("DocumentSnapshot added Success")
        }.addOnFailureListener { e ->
            Timber.w("Error adding document", e)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}