package com.darien.videosdata.di

import com.darien.videosdata.api.YoutubeVideosApi
import com.darien.videosdata.commons.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class YoutubeVideosModule {
    @Provides
    @Singleton
    fun provideYoutubeVideosApi(): YoutubeVideosApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YoutubeVideosApi::class.java)
    }
}