package com.nanotech.app_test.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.nanotech.app_test.data.errors.NoNetworkError
import com.nanotech.app_test.data.local.entity.User
import com.nanotech.app_test.data.repositories.IUserRepository
import com.nanotech.app_test.viewmodels.base.BaseViewModel
import com.nanotech.app_test.viewmodels.base.IViewModelFactory
import com.nanotech.app_test.viewmodels.base.IViewModelState
import com.nanotech.app_test.viewmodels.base.Notify
import javax.inject.Inject

class UserViewModel(
    handle: SavedStateHandle,
    private val repository: IUserRepository
) : BaseViewModel<UserState>(handle, UserState()) {

    private val usersList = MutableLiveData<List<User>>()

    fun observeResponse(owner: LifecycleOwner, onChange: (List<User>) -> Unit) {
        usersList.observe(owner, Observer { onChange(it) })
    }

    fun getUsers() {
        launchSafety(
            {
                when (it) {
                    is NoNetworkError -> {
                        getUsersFromDatabase()
                        notify(Notify.TextMessage("Network Not Available, failed upload users"))
                    }
                    else -> notify(Notify.ErrorMessage(it.message ?: "Something wrong"))
                }
            }
        ) {
            val cache = repository.getUsersFromDatabase()
            val content = repository.loadUsersFromNetwork()
            if (cache == content) {
                usersList.value = cache
            } else {
                repository.insertUsers(content)
                usersList.value = content
            }
        }
    }

    private fun getUsersFromDatabase() {
        launchSafety {
            val cache = repository.getUsersFromDatabase()
            if(cache.isNotEmpty()) usersList.value = cache
        }
    }

}

class UserViewModelFactory @Inject constructor(private val repository: IUserRepository) :
    IViewModelFactory<UserViewModel> {
    override fun create(handle: SavedStateHandle): UserViewModel {
        return UserViewModel(handle, repository)
    }
}

data class UserState(
    val users: List<User> = listOf<User>(),
) : IViewModelState {
    override fun save(outState: SavedStateHandle) {
        outState.set(::users.name, users)
    }

    override fun restore(savedState: SavedStateHandle): IViewModelState {
        return copy(
            users = savedState[::users.name] ?: listOf<User>()
        )
    }
}