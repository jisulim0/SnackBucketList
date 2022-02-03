package com.jisulim.snackcart.models.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wish")
data class WishEntity(
    @PrimaryKey val requester: String,
    @ColumnInfo(name = "product_name") val productName: String?,
    @ColumnInfo(name = "site_no") val siteNum: String,
    val image: String?,
    val description: String?,
    val price: Int,
    val ordQty: String,
    val itemId: String,
    var salestrNo: String
)