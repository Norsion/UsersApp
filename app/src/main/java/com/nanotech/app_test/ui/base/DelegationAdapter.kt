package com.nanotech.app_test.ui.base

import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class DelegationAdapter : ListDelegationAdapter<List<Any>>(AdapterDelegatesManager()) {

    val delegatesManager: AdapterDelegatesManager<List<Any>>
        get() = super.delegatesManager

    init {
        items = listOf()
    }

    override fun setItems(items: List<Any>) = setItems(items, null)

    fun setItems(items: List<Any>, payload: Any? = null) {
        val oldSize = this.items.size
        val newSize = items.size
        super.setItems(items)
        when {
            oldSize > newSize -> {
                notifyItemRangeRemoved(newSize, oldSize - newSize)
                notifyItemRangeChanged(0, newSize, payload)
            }
            newSize > oldSize -> {
                notifyItemRangeInserted(oldSize, newSize - oldSize)
                notifyItemRangeChanged(0, oldSize, payload)
            }
            else -> notifyItemRangeChanged(0, newSize, payload)
        }
    }

    fun forceSetItems(items: List<Any>) {
        this.setItems(items)
        notifyDataSetChanged()
    }
}