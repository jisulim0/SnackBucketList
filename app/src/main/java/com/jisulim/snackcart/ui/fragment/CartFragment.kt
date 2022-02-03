package com.jisulim.snackcart.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import com.jisulim.snackcart.R
import com.jisulim.snackcart.databinding.FragmentCartBinding
import com.jisulim.snackcart.models.dto.CartItem
import com.jisulim.snackcart.ui.activity.payment.PaymentActivity
import com.jisulim.snackcart.utils.FormatUtil


class CartFragment : BaseFragment() {

    private lateinit var binding: FragmentCartBinding
    lateinit var vhManager: ViewHolderManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        binding.apply {
            lifecycleOwner = this@CartFragment
            setViewHolderManager()
            rvSearchResult.layoutManager = LinearLayoutManager(activity)
            rvSearchResult.adapter =
                ListAdapter(vhManager, null)
        }

        viewModel.apply {
            userList = resources.getStringArray(R.array.user)
            refreshWishList()
            refreshTotalInfo()
        }

        setListener()
        setObserver()

        return binding.root
    }

    override fun setViewHolderManager() {
        vhManager = ViewHolderManager(viewModel, glide, LIST_TYPE.CART) { _, wish ->
            if (wish != null) {
                val title = "해당 상품을 삭제하시겠습니까?"
                val positive = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val isSuccess = viewModel.deleteGroup(wish.requester)
                        if (isSuccess) {
                            runOnUiThread {
                                toast("상품이 삭제되었습니다.")
                            }
                        } else {
                            runOnUiThread {
                                toast("다시 시도해주세요.")
                            }
                        }
                    }
                }
                dialog(title, positive = positive, negative = {})
            }
        }
    }

    override fun setObserver() {

        viewModel.wishList.observe(this, {
            (binding.rvSearchResult.adapter as ListAdapter).setData(it)
        })

        viewModel.totalCount.observe(this, {
            binding.tvTotalCount.text = (it ?: 0).toString()
            binding.tvNoti.visibility = viewModel.setTextCartIsEmpty()
        })

        viewModel.totalPrice.observe(this, {
            binding.tvTotalPrice.text = FormatUtil.priceFilter(it)
        })

    }

    override fun setListener() {
        binding.btnPayment.setOnClickListener {

            if (viewModel.wishList.value.isNullOrEmpty()) {
                toast("결제할 상품이 없습니다.")
                return@setOnClickListener
            }

            val sendData = mutableListOf<CartItem>()
            viewModel.wishList.value?.forEach { data ->
                val item = CartItem(
                    siteNo = data.siteNum,
                    itemId = data.itemId,
                    uitemId = "00000",
                    ordQty = data.ordQty,
                    salestrNo = data.salestrNo,
                    hopeShppDt = "",
                    cartPsblType = ""
                )
                sendData.add(item)
            }

            startActivity<PaymentActivity>(
                "cart" to sendData as ArrayList<out Parcelable>
            )
        }
    }

}