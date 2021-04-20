package com.nanotech.app_test.data.interceptor

import com.nanotech.app_test.app.utils.NetworkMonitor
import com.nanotech.app_test.data.errors.NoNetworkError
import okhttp3.Interceptor
import okhttp3.Response

class NetworkStatusInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkMonitor.isConnected) throw NoNetworkError()

        return chain.proceed(chain.request())
    }
}