package com.jisulim.snackcart.ui

import com.jisulim.snackcart.R

enum class SiteType(
    val baseUrl: String,
    val categoryFilterId: Int,
    val siteNo: String
) {
    EMART("http://emart.ssg.com", R.array.category_list_emart,  "6001"),
    TRADERS("http://traders.ssg.com", R.array.category_list_traders, "6002"),
}

