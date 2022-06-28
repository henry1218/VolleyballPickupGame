package com.volleyball.pickup.game.utils

import com.facebook.Profile
import javax.inject.Inject

class ProfileUtils @Inject constructor(private val profile: Profile?) {

    fun getName(): String = "${profile?.firstName} ${profile?.lastName}"

    fun getSmallProfilePic(): String = profile?.getProfilePictureUri(100, 100).toString()

    fun getLargeProfilePic(): String = profile?.getProfilePictureUri(200, 200).toString()
}