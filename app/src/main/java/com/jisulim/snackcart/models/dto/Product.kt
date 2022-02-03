package com.jisulim.snackcart.models.dto

// search fragment
data class Product(
    val siteNum: String,
    val productName: String?,
    val image: String?,
    val description: String?,
    val price: Int,
    val ordQty: String,
    val itemId: String,
    var salestrNo: String
)