package com.nanotech.app_test.app

import android.app.Application
import com.nanotech.app_test.BuildConfig
import com.nanotech.app_test.app.di.AppComponent
import com.nanotech.app_test.app.di.DaggerAppComponent
import com.nanotech.app_test.app.utils.NetworkMonitor
import timber.log.Timber

class App : Application(), AppComponent.ComponentProvider {

    override lateinit var appComponent: AppComponent

    companion object {
        lateinit var INSTANCE: App
            private set
    }

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        NetworkMonitor.registerNetworkMonitor(applicationContext)

        appComponent.inject(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}