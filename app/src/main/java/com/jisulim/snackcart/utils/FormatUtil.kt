package com.jisulim.snackcart.utils

import com.google.gson.Gson
import com.jisulim.snackcart.models.dto.Cart
import java.text.NumberFormat
import java.util.*

object FormatUtil {

    private val numbFormat = NumberFormat.getInstance(Locale.KOREA)

    fun priceStringToInt(strPrice: String) = strPrice.replace(",", "").toInt()
    fun priceFilter(price: Int): String = numbFormat.format(price)

    fun dataToString(data : Any) = Gson().toJson(data)

    fun jsItemCartUrl(item: Cart) = "javascript:ItemCart.cart(${dataToString(item)})"

}