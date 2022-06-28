package com.volleyball.pickup.game.di

import com.facebook.Profile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FacebookModule {

    @Provides
    fun provideProfile(): Profile? {
        return Profile.getCurrentProfile()
    }
}