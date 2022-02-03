package com.jisulim.snackcart.models.dto


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// cart fragment

data class Cart(
    val cartTypeCd: String,
    val infloSiteNo: String,
    val items: List<CartItem>?
)

@Parcelize
data class CartItem(
    val siteNo: String,
    val itemId: String,
    val uitemId: String,
    val ordQty: String,
    val salestrNo: String,
    val hopeShppDt: String,
    val cartPsblType: String
) : Parcelable