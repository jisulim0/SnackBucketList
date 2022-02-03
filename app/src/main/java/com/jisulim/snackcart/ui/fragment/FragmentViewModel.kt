package com.jisulim.snackcart.ui.fragment

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.jisulim.snackcart.data.repository.WishRepository
import com.jisulim.snackcart.models.dto.Product
import com.jisulim.snackcart.models.entity.WishEntity
import com.jisulim.snackcart.ui.SiteType
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FragmentViewModel @Inject constructor(
    private val repository: WishRepository
) : ViewModel() {

    // 등록된 사용자
    lateinit var userList: Array<String>

    // 검색 결과 리스트
    private var _searchResultList = MutableLiveData<List<Product>?>()
    val searchResultList: LiveData<List<Product>?>
        get() = _searchResultList

    // 장바구니 리스트
    private var _wishList = MutableLiveData<List<WishEntity>?>()
    val wishList: LiveData<List<WishEntity>?>
        get() = _wishList

    // 선택된 유저 인덱스
    private var _requesterId = MutableLiveData<Int>()
    val requesterId: LiveData<Int>
        get() = _requesterId

    // 선택된 검색 사이트
    private var _siteType = MutableLiveData<SiteType>()
    val siteType: LiveData<SiteType>
        get() = _siteType

    // 총 금액
    private var _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int>
        get() = _totalPrice

    // 총 개수
    private var _totalCount = MutableLiveData<Int>()
    val totalCount: LiveData<Int>
        get() = _totalCount

    // (livedata) 상태 업데이트
    fun refreshSearchResultList(data: List<Product>?) {
        viewModelScope.launch {
            _searchResultList.value = data
        }
    }

    fun refreshRequesterId(position: Int) {
        viewModelScope.launch {
            _requesterId.value = position
        }
    }

    fun refreshSiteType(site: SiteType) {
        viewModelScope.launch {
            _siteType.value = site
        }
    }

    fun refreshWishList() {
        viewModelScope.launch {
            _wishList.value = roomGetWishList()
        }
    }


    fun refreshTotalInfo() {
        viewModelScope.launch {
            _totalCount.value = roomGetCount()
            _totalPrice.value = roomGetPrice()
        }
    }


    // repository update
    fun roomInsert(item: Product) {
        val requester = userList[requesterId.value ?: 0]

        val data = WishEntity(
            requester = requester,
            productName = item.productName,
            siteNum = siteType.value!!.siteNo,
            image = item.image,
            description = item.description,
            price = item.price,
            ordQty = item.ordQty,
            itemId = item.itemId,
            salestrNo = item.salestrNo
//
//            url = item.url,
//            updateAt = Date().toString()
        )
        viewModelScope.launch {
            repository.insert(data)
        }
    }

    fun roomUpdate(item: Product) {
        val requester = userList[requesterId.value ?: 0]

        val data = WishEntity(
            requester = requester,
            productName = item.productName,
            siteNum = siteType.value!!.siteNo,
            image = item.image,
            description = item.description,
            price = item.price,
            ordQty = item.ordQty,
            itemId = item.itemId,
            salestrNo = item.salestrNo
//            url = item.url,
//            updateAt = Date().toString()
        )
        viewModelScope.launch {
            repository.update(data)
        }
    }

    suspend fun deleteGroup(requester: String) = withContext(viewModelScope.coroutineContext) {
        if (roomDelete(requester) > 0) {
            refreshWishList()
            refreshTotalInfo()
            return@withContext true
        } else {
            return@withContext false
        }
    }


    private suspend fun roomDelete(requester: String): Int =
        withContext(viewModelScope.coroutineContext) {
            return@withContext repository.delete(requester)
        }


    suspend fun roomRequesterIsExist(): Boolean {
        // 사용자가 이미 상품을 등록했는지
        return withContext(viewModelScope.coroutineContext) {
            val requester = userList[requesterId.value ?: 0]
            repository.isExist(requester)
        }
    }

    private suspend fun roomGetWishList(): List<WishEntity>? {
        return withContext(viewModelScope.coroutineContext) {
            repository.getWish()
        }
    }

    private suspend fun roomGetCount(): Int {
        return withContext(viewModelScope.coroutineContext) {
            repository.getCount() ?: 0
        }
    }

    private suspend fun roomGetPrice(): Int {
        return withContext(viewModelScope.coroutineContext) {
            repository.getPrice() ?: 0
        }
    }

    fun setTextCartIsEmpty(): Int =
        if (totalCount.value != 0) {
            View.GONE
        } else {
            View.VISIBLE
        }

    fun setTextSearchIsEmpty(): Int =
        if (searchResultList.value?.size != 0) {
            View.GONE
        } else {
            View.VISIBLE
        }

}