package com.nanotech.app_test.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nanotech.app_test.BuildConfig
import com.nanotech.app_test.data.local.dao.UserDao
import com.nanotech.app_test.data.local.entity.User

@Database(entities = [User::class], version = UsersDatabase.DATABASE_VERSION, exportSchema = false)
abstract class UsersDatabase() : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = BuildConfig.APPLICATION_ID + ".db"
        const val DATABASE_VERSION = 1
    }

    abstract fun userDao(): UserDao
}