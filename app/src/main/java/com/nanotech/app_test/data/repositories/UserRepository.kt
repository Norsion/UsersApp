package com.nanotech.app_test.data.repositories

import com.nanotech.app_test.app.extensions.data.toUser
import com.nanotech.app_test.data.api.ApiUsers
import com.nanotech.app_test.data.local.UsersDatabase
import com.nanotech.app_test.data.local.entity.User

class UserRepository(
    private val userApi: ApiUsers,
    private val usersDatabase: UsersDatabase
) : IUserRepository {

    private val usersDao = usersDatabase.userDao()

    override suspend fun getUsersFromDatabase() = usersDao.getUsers()

    override suspend fun loadUsersFromNetwork() :List<User> {
        return userApi.getUsers().map { userRes -> userRes.toUser() }
    }

    override suspend fun insertUsers(users: List<User>) {
        usersDao.saveUsers(users)
    }
}