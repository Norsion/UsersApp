package com.nanotech.app_test.data.api

import com.nanotech.app_test.data.remote.UserRes
import retrofit2.http.GET

interface ApiUsers {
    @GET("/users")
    suspend fun getUsers():List<UserRes>
}