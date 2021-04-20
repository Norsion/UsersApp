package com.nanotech.app_test.app.extensions.data

import com.nanotech.app_test.data.local.entity.User
import com.nanotech.app_test.data.remote.UserRes

fun UserRes.toUser(): User = User(
    id = id,
    username = username)