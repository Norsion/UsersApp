package com.nanotech.app_test.data.errors

import java.io.IOException

sealed class ApiError(override val message:String): IOException(message) {
    class BadRequest(message: String?) : ApiError(message?: "Bad Request")
    class NotFound(message: String? ) : ApiError(message ?: "Not found")
    class InternalServerError(message: String?) : ApiError(message ?: "Internal server error")
    class UnknownError(message: String?) : ApiError(message ?: "Unknown error" )
}

class ErrorBody(val message:String)