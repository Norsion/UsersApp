package com.nanotech.app_test.app.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nanotech.app_test.app.utils.Event
import com.nanotech.app_test.app.utils.EventObserver
import com.nanotech.app_test.app.utils.Failure

/**
 * Fragment should use [Fragment.getViewLifecycleOwner] instead this to avoid leaking LiveData observers in Fragments
 * https://proandroiddev.com/5-common-mistakes-when-using-architecture-components-403e9899f4cb
 */
fun <T : Any?, L : LiveData<T>> Fragment.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(viewLifecycleOwner, Observer(body))

fun <T : Any?, L : LiveData<Event<T>>> Fragment.observeEvent(liveData: L, body: (T) -> Unit) =
    liveData.observe(viewLifecycleOwner, EventObserver(body))

fun <L : LiveData<Failure>> Fragment.failure(liveData: L, body: (Failure?) -> Unit) =
    liveData.observe(viewLifecycleOwner, Observer(body))

fun <T : Any?, L : LiveData<T>> AppCompatActivity.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

fun <T : Any?, L : LiveData<Event<T>>> AppCompatActivity.observeEvent(
    liveData: L,
    body: (T) -> Unit
) =
    liveData.observe(this, EventObserver(body))