package com.nanotech.app_test.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.nanotech.app_test.viewmodels.base.BaseViewModel
import com.nanotech.app_test.viewmodels.base.IViewModelFactory
import com.nanotech.app_test.viewmodels.base.IViewModelState
import javax.inject.Inject

class MainViewModel(
    handle: SavedStateHandle
) : BaseViewModel<MainState>(handle, MainState()) {


}

class MainViewModelFactory @Inject constructor() : IViewModelFactory<MainViewModel> {
    override fun create(handle: SavedStateHandle): MainViewModel {
        return MainViewModel(handle)
    }
}

class MainState() : IViewModelState