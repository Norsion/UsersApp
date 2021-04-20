package com.nanotech.app_test.app.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.nanotech.app_test.BuildConfig
import com.nanotech.app_test.data.local.UsersDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    fun provideApplicationContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideUserDatabase(context: Context) : UsersDatabase = Room
        .databaseBuilder(context, UsersDatabase::class.java, UsersDatabase.DATABASE_NAME)
        .run { if (BuildConfig.DEBUG) fallbackToDestructiveMigration() else this }
        .build()

}