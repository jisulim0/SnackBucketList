package com.jisulim.snackcart.di.module

import android.content.Context
import com.bumptech.glide.Glide
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.jisulim.snackcart.utils.PrefUtil
import javax.inject.Singleton

// 즉!!!!!!!!!! 여기서 객체를 대신 만드는거지

@Module
@InstallIn(SingletonComponent::class)
object ImageModule {

    @Singleton
    @Provides
    fun provideGlide(@ApplicationContext context: Context) = Glide.with(context)

}

@Module
@InstallIn(SingletonComponent::class)
object PrefModule {
    @Singleton
    @Provides
    fun providePref(@ApplicationContext context: Context): PrefUtil = PrefUtil(context)
}