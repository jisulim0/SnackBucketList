package com.jisulim.snackcart.utils

import android.content.res.Resources
import android.util.Log
import com.jisulim.snackcart.SCApplication
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import com.jisulim.snackcart.models.dto.Product
import com.jisulim.snackcart.ui.SiteType
import kotlin.concurrent.thread

object Jsoup {

    private const val target = "all"
    private const val count = 100

    lateinit var resources: Resources
    lateinit var doc: Document

    private var select: SiteType = SiteType.EMART

//    fun init(resource: Resources) {
//        resources = resource
//    }

    fun changeSiteType(type: SiteType) {
        select = type
    }

    fun getCartCount(): Int? {
        val requestUrl = "https://pay.ssg.com/m/cart/dmsShpp.ssg"

        thread {
            doc = Jsoup.connect(requestUrl).get()
        }.join()

        val result = doc
            .select("div.mnodr_total")
            .select("div.mnodr_form_sec")
            .select("dl.mnodr_priceitem.ty_total")
            .select("dd")
            .select("span.mnodr_priceitem_total")
            .select("em.ssg_price.viewAmt_paymt")
            .text()

        return try {
            FormatUtil.priceStringToInt(result)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun search(query: String): MutableList<Product> {

        val requestUrl = parseUrl(query)

        Log.d("Crawling_Log", "requestUrl = $requestUrl")

//        try {
//            doc = Jsoup.connect(requestUrl)
//                .userAgent("mozilla/5.0 (windows nt 6.1; wow64) applewebkit/537.36 (khtml, gecko) chrome/36.0.1985.143 safari/537.36")
//                .get()
//        } catch (e: HttpStatusException) {
//            e.printStackTrace()
//            return null
//        }

        try {

            doc = Jsoup.connect(requestUrl).get()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return mutableListOf()
        }

        val items = doc.select("#idProductImg li")


        val searchResult = mutableListOf<Product>()

        items.forEach { item ->
            val info = item.select("div.cunit_info")
            val prod = item.select("div.cunit_prod")

            val prodInfo = info.select("div.cunit_md.notranslate").select("div")

            val productName = prodInfo.select("strong").select("em.tx_ko").text()
            val description = prodInfo.select("a").select("em.tx_ko").text()

            val priceInfo = info.select("div.cunit_price.notranslate")

            val price = priceInfo.select("div.opt_price").select("em.ssg_price").text()

            val thmb = prod.select("div.thmb")

            val image = thmb.select("a").select("img").attr("src")
            val ordQty =
                thmb.select("div.exp_area.notranslate").select("div.util_bx").select("span")
                    .attr("data-cart-ordqty")
            val salestrNo = thmb.select("div.exp_area.notranslate").select("div.util_bx")
                .select("span.cmlike._js_cmlike.interestIt").select("input[name=attnTgtIdnfNo2]")
                .attr("value")
            val itemId = prod.attr("data-react-unit-id")

            val addItem = Product(
                siteNum = select.siteNo,
                productName = productName,
                description = description,
                price = FormatUtil.priceStringToInt(price),
                image = image,
                ordQty = ordQty,
                itemId = itemId,
                salestrNo = salestrNo
            )

            searchResult.add(addItem)
        }

        return searchResult
    }

    private fun parseUrl(query: String): String {
        val base = select.baseUrl
        val default = "/search.ssg?target=${target}&query=${query}&count=${count}"
        val categoryIdFilter = setQuery(SCApplication.resource.getStringArray(select.categoryFilterId))

        return base + default + categoryIdFilter
    }

    private fun setQuery(data: Array<String>): String {
        var urlQuery = ""
        data.forEach {
            urlQuery += "&ctgId=${it}"
        }

        return urlQuery
    }

}

