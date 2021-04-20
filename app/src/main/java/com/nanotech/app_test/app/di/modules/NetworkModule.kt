package com.nanotech.app_test.app.di.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.moczul.ok2curl.CurlInterceptor
import com.nanotech.app_test.BuildConfig
import com.nanotech.app_test.data.api.ApiUsers
import com.nanotech.app_test.data.interceptor.ErrorStatusInterceptor
import com.nanotech.app_test.data.interceptor.NetworkStatusInterceptor
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton
@Module
class NetworkModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class CoroutineScopeIO

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @CoroutineScopeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)

    @Provides
    fun provideNetworkStatusInterceptor() = NetworkStatusInterceptor()

    @Provides
    @Singleton
    @Named(WITHOUT_AUTH_CLIENT)
    fun provideOkHttpClient(
        networkStatusInterceptor: NetworkStatusInterceptor,
        errorStatusInterceptor: ErrorStatusInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(networkStatusInterceptor)
            .addInterceptor(errorStatusInterceptor)
            .addInterceptor(CurlInterceptor {
                Timber.v(it)
            })
            .build()

    @Provides
    @Named(AUTH_CLIENT)
    fun provideAuthOkHttpClient(
        networkStatusInterceptor: NetworkStatusInterceptor,
        errorStatusInterceptor: ErrorStatusInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(networkStatusInterceptor)
            .addInterceptor(errorStatusInterceptor)
            .addInterceptor(CurlInterceptor {
                Timber.v(it)
            })
            .build()

    @Provides
    @Singleton
    @Named(WITHOUT_AUTH_RETROFIT)
    fun provideRetrofit(gson: Gson, @Named(WITHOUT_AUTH_CLIENT) okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Provides
    @Named(AUTH_RETROFIT)
    fun provideAuthRetrofit(gson: Gson, @Named(AUTH_CLIENT) okHttpClient: OkHttpClient) : Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Provides
    fun provideUsersApi(@Named(AUTH_RETROFIT) retrofit: Retrofit) : ApiUsers =
        retrofit.create(ApiUsers::class.java)

    companion object {
        private const val WITHOUT_AUTH_CLIENT = "without_auth_client"
        private const val WITHOUT_AUTH_RETROFIT = "without_auth_retrofit"

        private const val AUTH_CLIENT = "auth_client"
        private const val AUTH_RETROFIT = "auth_retrofit"
    }
}