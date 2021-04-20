package com.nanotech.app_test.ui.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nanotech.app_test.R
import com.nanotech.app_test.data.local.entity.User
import com.nanotech.app_test.ui.base.BaseAdapterDelegate
import kotlinx.android.synthetic.main.item_user.view.*

class UsersDelegate : BaseAdapterDelegate() {

    override fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return UserViewHolder(inflater.inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(item: Any, holder: ViewHolder, payloads: MutableList<Any>) {
        val viewHolder = holder as UserViewHolder
        viewHolder.bind(item as User)
    }

    override fun isForItem(item: Any, items: MutableList<Any>, position: Int): Boolean = item is User

    inner class UserViewHolder(convertView: View) : ViewHolder(convertView) {
        fun bind(item: User) {
            itemView.tv_user_id.text = item.id.toString()
            itemView.tv_username.text = item.username
        }
    }
}