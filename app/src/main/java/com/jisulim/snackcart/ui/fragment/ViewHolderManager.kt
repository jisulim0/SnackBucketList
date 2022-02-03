package com.jisulim.snackcart.ui.fragment

import com.bumptech.glide.RequestManager
import com.jisulim.snackcart.models.dto.Product
import com.jisulim.snackcart.models.entity.WishEntity

class ViewHolderManager(
    val viewModel: FragmentViewModel,
    val glide: RequestManager,
    val type: LIST_TYPE,
    val listener: (Product?, WishEntity?) -> Unit,
)