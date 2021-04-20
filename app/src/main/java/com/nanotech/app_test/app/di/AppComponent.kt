package com.nanotech.app_test.app.di

import android.app.Application
import com.nanotech.app_test.ui.MainActivity
import com.nanotech.app_test.app.App
import com.nanotech.app_test.app.di.modules.AppModule
import com.nanotech.app_test.app.di.modules.NetworkModule
import com.nanotech.app_test.app.di.modules.RepositoryModule
import com.nanotech.app_test.ui.users.UserFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {
    fun inject(application: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    interface ComponentProvider {
        val appComponent: AppComponent
    }

    //activity, fragment
    fun inject(activity: MainActivity)
    fun inject(activity: UserFragment)
}