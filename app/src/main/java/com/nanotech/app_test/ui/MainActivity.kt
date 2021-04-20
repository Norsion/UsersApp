package com.nanotech.app_test.ui

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.nanotech.app_test.R
import com.nanotech.app_test.app.App
import com.nanotech.app_test.ui.base.BaseActivity
import com.nanotech.app_test.viewmodels.MainState
import com.nanotech.app_test.viewmodels.MainViewModel
import com.nanotech.app_test.viewmodels.MainViewModelFactory
import com.nanotech.app_test.viewmodels.base.IViewModelState
import com.nanotech.app_test.viewmodels.base.Notify
import com.nanotech.app_test.viewmodels.base.SavedStateViewModelFactory
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel>() {

    init {
        App.INSTANCE.appComponent.inject(this@MainActivity)
    }

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    override val layout: Int = R.layout.activity_main
    public override val viewModel: MainViewModel by viewModels {
        SavedStateViewModelFactory(mainViewModelFactory, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navGraph = findNavController(R.id.nav_host_fragment).graph
        navGraph.startDestination = R.id.userFragment
        navController.graph = navGraph

    }

    override fun subscribeOnState(state: IViewModelState) {
        state as MainState
    }

    override fun renderNotification(notify: Notify) {
        when (notify) {
            is Notify.ActionMessage -> {
                val builder = AlertDialog.Builder(this)
                builder.apply {
                    setTitle(notify.actionLabel)
                    setMessage(notify.message)
                    setPositiveButton(
                        "Хорошо"
                    ) { dialog, id ->
                        notify.actionHandler.invoke()
                        dialog.dismiss()
                    }
                }
                builder.show()
            }
            is Notify.TextMessage -> {
                Toast.makeText(this, notify.message, Toast.LENGTH_SHORT).show()
            }
            is Notify.ErrorMessage -> {
                Toast.makeText(this, notify.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (
                v is SearchView.SearchAutoComplete ||
                v is EditText ||
                v is TextInputEditText
            ) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}