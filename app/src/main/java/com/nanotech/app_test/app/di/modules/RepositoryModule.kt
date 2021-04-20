package com.nanotech.app_test.app.di.modules

import androidx.room.Room
import com.nanotech.app_test.BuildConfig
import com.nanotech.app_test.app.App
import com.nanotech.app_test.data.api.ApiUsers
import com.nanotech.app_test.data.local.UsersDatabase
import com.nanotech.app_test.data.local.dao.UserDao
import com.nanotech.app_test.data.repositories.IUserRepository
import com.nanotech.app_test.data.repositories.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    fun provideUserRepository(
        userApi: ApiUsers,
        usersDatabase: UsersDatabase
    ) : IUserRepository = UserRepository(userApi, usersDatabase)

}