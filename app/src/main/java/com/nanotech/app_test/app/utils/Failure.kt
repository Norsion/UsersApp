package com.nanotech.app_test.app.utils

sealed class Failure {
    object NetworkConnection : Failure()
    object CommonError : Failure()
}