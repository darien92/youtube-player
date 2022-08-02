package com.darien.data.di

import android.content.Context
import androidx.room.Room
import com.darien.data.commons.Commons
import com.darien.data.db.DatabaseService
import com.darien.data.db.WordsDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchModule {
    @Provides
    @Singleton
    fun provideSearchDbService(@ApplicationContext context: Context): DatabaseService {
        return Room.databaseBuilder(
            context, DatabaseService::class.java,
            Commons.DbConstants.dbName
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideWordsDao(wordsDatabase: DatabaseService): WordsDAO = wordsDatabase.wordsDao()
}