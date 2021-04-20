package com.nanotech.app_test.ui.users

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nanotech.app_test.R
import com.nanotech.app_test.app.App
import com.nanotech.app_test.ui.base.BaseFragment
import com.nanotech.app_test.ui.base.DelegationAdapter
import com.nanotech.app_test.ui.base.ToolbarBuilder
import com.nanotech.app_test.viewmodels.UserViewModel
import com.nanotech.app_test.viewmodels.UserViewModelFactory
import com.nanotech.app_test.viewmodels.base.SavedStateViewModelFactory
import kotlinx.android.synthetic.main.fragment_users.*
import javax.inject.Inject

class UserFragment : BaseFragment<UserViewModel>() {

    init {
        App.INSTANCE.appComponent.inject(this@UserFragment)
    }

    @Inject
    internal lateinit var userViewModelFactory: UserViewModelFactory

    override val viewModel: UserViewModel by viewModels {
        SavedStateViewModelFactory(userViewModelFactory, this)
    }
    override val layout: Int = R.layout.fragment_users

    private val userAdapter by lazy {  DelegationAdapter() }

    override val prepareToolbar: (ToolbarBuilder.() -> Unit)? = {
        setTitle(resources.getString(R.string.user_fragment_title))
        this.isBackButtonVisible = false
    }

    override fun setupViews() {
        viewModel.getUsers()
        initAdapter()
        viewModel.observeResponse(viewLifecycleOwner) {
            userAdapter.items = it
        }
    }

    private fun initAdapter() {
        userAdapter.delegatesManager.apply {
            addDelegate(UsersDelegate())
        }

        rv_users.apply {
            addItemDecoration(
                ItemDecorator(
                    divider = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.divider
                    )
                )
            )
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = userAdapter
        }
    }
}