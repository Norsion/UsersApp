package com.nanotech.app_test.ui.base

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.nanotech.app_test.R
import com.nanotech.app_test.viewmodels.base.*
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseActivity<T : BaseViewModel<out IViewModelState>> : AppCompatActivity() {
    protected abstract val viewModel: T
    protected abstract val layout: Int
    lateinit var navController: NavController

    val toolbarBuilder = ToolbarBuilder()

    abstract fun subscribeOnState(state: IViewModelState)

    abstract fun renderNotification(notify: Notify)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        setSupportActionBar(toolbar)
        viewModel.observeState(this) { subscribeOnState(it) }
        viewModel.observeNotifications(this) { renderNotification(it) }
        viewModel.observeNavigation(this) { subscribeOnNavigation(it) }
        viewModel.observeLoading(this) { renderLoading(it) }

        navController = findNavController(R.id.nav_host_fragment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState()
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.restoreState()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    open fun renderLoading(loadingState: Loading) {
        when (loadingState) {
            Loading.SHOW_LOADING -> progress.visibility = View.VISIBLE
            Loading.SHOW_BLOCKING_LOADING -> {
                progress.visibility = View.VISIBLE
                //TODO block interact with UI
            }
            Loading.HIDE_LOADING -> progress.visibility = View.GONE
        }
    }

    private fun subscribeOnNavigation(command: NavigationCommand) {
        if (command is NavigationCommand.To) {
            navController.navigate(
                command.destination,
                command.args,
                command.options,
                command.extras
            )
        }
        if (command is NavigationCommand.Back) {
            navController.popBackStack()
        }
    }
}

class ToolbarBuilder {
    var title: String? = ""
    var titleColor: Int = 0
    var isBackgroundLight = false
    var isBackButtonVisible: Boolean = true
    var visibility: Boolean = true
    val items: MutableList<MenuItemHolder> = mutableListOf()

    fun setTitle(title: String) = apply { this.title = title }
    fun setTitleColor(titleColor: Int) = apply { this.titleColor = titleColor }
    fun setBackgroundLight(isLight: Boolean) = apply { this.isBackgroundLight = isLight }
    fun setBackButtonVisible(isVisible: Boolean) = apply { this.isBackButtonVisible = isVisible }
    fun addMenuItem(item: MenuItemHolder) = apply { this.items.add(item) }

    fun invalidate() = apply {
        title = null
        titleColor = 0
        isBackgroundLight = false
        isBackButtonVisible = true
        visibility = true
        items.clear()
    }

    fun prepare(prepareFn: (ToolbarBuilder.() -> Unit)?) = apply { prepareFn?.invoke(this) }

    fun build(context: FragmentActivity) {

        with(context.toolbar) {
            isVisible = this@ToolbarBuilder.visibility
            contentInsetStartWithNavigation = 0

            if (this@ToolbarBuilder.visibility) {
                if (this@ToolbarBuilder.title != null) {
                    this.title = this@ToolbarBuilder.title

                    val colorId = if (this@ToolbarBuilder.isBackgroundLight) {
                        R.color.black
                    } else {
                        R.color.white
                    }
                    setTitleTextColor(ContextCompat.getColor(this.context, colorId))
                }
                if (this@ToolbarBuilder.titleColor != 0) {
                    setTitleTextColor(this@ToolbarBuilder.titleColor)
                }
                if (this@ToolbarBuilder.isBackButtonVisible.not()) {
                    this.navigationIcon = null
                } else {
                    val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_back, null)
                    if (drawable != null) {
                        val colorId = if (this@ToolbarBuilder.isBackgroundLight) {
                            R.color.black
                        } else {
                            R.color.white
                        }
                        drawable.setTint(ContextCompat.getColor(this.context, colorId))
                        this.navigationIcon = drawable
                    }
                }

                val colorId = if (this@ToolbarBuilder.isBackgroundLight) {
                    R.color.white
                } else {
                    R.color.colorPrimary
                }
                setBackgroundColor(ContextCompat.getColor(this.context, colorId))
            }
        }
    }
}

data class MenuItemHolder(
    val title: String,
    val menuId: Int,
    var icon: Int? = null,
    val actionViewLayout: Int? = null,
    var visible:Boolean = true,
    val clickListener: ((MenuItem) -> Unit)? = null
)
