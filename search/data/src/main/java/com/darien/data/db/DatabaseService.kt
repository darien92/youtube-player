package com.darien.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.darien.data.commons.Commons.DbConstants.dbVersion

/**
 * In this case we will only use Database for this module
 */
@Database(entities = [WordsEntity::class], version = dbVersion)
abstract class DatabaseService: RoomDatabase() {
    abstract fun wordsDao(): WordsDAO
}