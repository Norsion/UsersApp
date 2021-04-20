package com.nanotech.app_test.data.interceptor

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.nanotech.app_test.data.errors.ApiError
import com.nanotech.app_test.data.errors.ErrorBody
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class ErrorStatusInterceptor @Inject constructor(
    private val gson: Gson
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val res = chain.proceed(chain.request())

        if (res.isSuccessful) return res

        val errMessage = try {
            gson.fromJson(res.body!!.string(), ErrorBody::class.java)?.message
        } catch (e: JsonParseException) {
            e.message
        }

        when (res.code) {
            400 -> throw ApiError.BadRequest(errMessage)
            404 -> throw ApiError.NotFound(errMessage)
            500 -> throw ApiError.InternalServerError(errMessage)
            else -> throw ApiError.UnknownError(errMessage)
        }
    }
}