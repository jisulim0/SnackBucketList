package com.jisulim.snackcart.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jisulim.snackcart.R
import com.jisulim.snackcart.databinding.ItemProductionBinding

enum class LIST_TYPE {
    SEARCH, CART
}

class ListAdapter(
    private val vhManager: ViewHolderManager,
    private val items: List<*>?,
) :
    RecyclerView.Adapter<ViewHolder>() {

    var data: List<*>?

    init {
        data = items
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        DataBindingUtil.inflate<ItemProductionBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_production,
            parent,
            false
        ).let {
            ViewHolder(it, vhManager)
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data.isNullOrEmpty()) {
            return
        }
        holder.bind(data!![holder.adapterPosition])
    }

    override fun getItemCount(): Int = data?.size ?: 0

    @JvmName("refreshListData")
    fun setData(refreshData: List<*>?) {
        data = refreshData
        notifyDataSetChanged()
    }
}