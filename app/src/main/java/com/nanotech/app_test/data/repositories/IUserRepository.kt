package com.nanotech.app_test.data.repositories

import com.nanotech.app_test.data.local.entity.User


interface IUserRepository {

    suspend fun getUsersFromDatabase():List<User>

    suspend fun loadUsersFromNetwork():List<User>

    suspend fun insertUsers(users:List<User>)
}