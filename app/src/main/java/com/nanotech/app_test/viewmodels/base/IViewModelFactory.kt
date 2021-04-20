package com.nanotech.app_test.viewmodels.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle

interface IViewModelFactory<T : ViewModel> {
    fun create(handle: SavedStateHandle): T
}