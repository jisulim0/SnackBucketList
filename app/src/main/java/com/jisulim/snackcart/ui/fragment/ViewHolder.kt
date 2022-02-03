package com.jisulim.snackcart.ui.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jisulim.snackcart.databinding.ItemProductionBinding
import com.jisulim.snackcart.models.dto.Product
import com.jisulim.snackcart.models.entity.WishEntity
import com.jisulim.snackcart.utils.FormatUtil


class ViewHolder(
    private val binding: ItemProductionBinding,
    private val vhManager: ViewHolderManager,
) : RecyclerView.ViewHolder(binding.root) {

    var requestOptions: RequestOptions =
        RequestOptions().transforms(CenterCrop(), RoundedCorners(16))

    fun bind(data: Any?) {
        when (vhManager.type) {
            LIST_TYPE.SEARCH -> searchBind(data as Product)
            LIST_TYPE.CART -> cartBind(data as WishEntity)
        }
    }

    private fun searchBind(item: Product) {
        binding.apply {

            if(item.productName.isNullOrEmpty()){
                tvProductName.visibility = View.GONE
            }

            tvRequester.visibility = View.GONE
            btnDelete.visibility = View.GONE

            tvProductName.text = textFilter(item.productName)
            tvDescription.text = textFilter(item.description)
            tvPrice.text = FormatUtil.priceFilter(item.price)
            container4.visibility = View.GONE
        }
        vhManager.glide.load("http:" + item.image).apply(requestOptions).into(binding.ivProduct)

        binding.root.setOnClickListener {
            vhManager.listener(item, null)
        }
    }

    private fun cartBind(item: WishEntity) {
        binding.apply {

            if(item.productName.isNullOrEmpty()){
                tvProductName.visibility = View.GONE
            }

            tvProductName.text = textFilter(item.productName)
            tvDescription.text = textFilter(item.description)
            tvPrice.text = FormatUtil.priceFilter(item.price)
            btnDelete.visibility = View.VISIBLE
            tvRequester.text = item.requester
        }
        vhManager.glide.load("http:" + item.image).apply(requestOptions).into(binding.ivProduct)

        binding.btnDelete.setOnClickListener {
            vhManager.listener(null, item)
        }
    }

    private fun textFilter(text: String?) = when (text.isNullOrEmpty()) {
        true -> ""
        false -> text
    }


}