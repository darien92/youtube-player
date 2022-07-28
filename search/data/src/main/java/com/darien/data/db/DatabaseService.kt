package com.darien.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.darien.data.commons.Commons.DbConstants.dbName
import com.darien.data.commons.Commons.DbConstants.dbVersion

/**
 * In this case we will only use Database for this module
 */
@Database(entities = [WordsEntity::class], version = dbVersion)
abstract class DatabaseService: RoomDatabase() {
    companion object{
        private var instance: DatabaseService? = null
        private fun create(context: Context): DatabaseService =
            Room.databaseBuilder(context, DatabaseService::class.java, dbName)
                .fallbackToDestructiveMigration()
                .build()

        fun getInstance(context: Context): DatabaseService =
            (instance ?: create(context)).also { instance = it }
    }

    abstract fun wordsDao(): WordsDAO
}