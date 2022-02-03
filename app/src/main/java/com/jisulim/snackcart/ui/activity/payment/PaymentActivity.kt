package com.jisulim.snackcart.ui.activity.payment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import com.jisulim.snackcart.R
import com.jisulim.snackcart.databinding.ActivityPaymentBinding
import com.jisulim.snackcart.models.dto.Cart
import com.jisulim.snackcart.models.dto.CartItem
import com.jisulim.snackcart.ui.State
import com.jisulim.snackcart.ui.activity.BaseActivity
import com.jisulim.snackcart.utils.*
import javax.inject.Inject


@AndroidEntryPoint
class PaymentActivity : BaseActivity() {

    private val viewModel: PaymentViewModel by viewModels()
    private val binding by binding<ActivityPaymentBinding>(R.layout.activity_payment)

    lateinit var webSetting: WebSettings

    @Inject
    lateinit var prefUtil: PrefUtil


    var cartIsEmpty = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        viewModel.wishList = intent.extras?.getParcelableArrayList<CartItem>("cart")!!.toList()

//        if()

    }

    override fun onResume() {
        super.onResume()

//        CookieSyncManager.getInstance().startSync()
        showLoading(true)

        webViewInit()
        viewModel.updateState(State.INIT)
    }

    private fun webViewInit() {
        binding.webView.apply {
            webChromeClient = webViewPopUpInterrupt()
            webViewClient = pageFinishListener()
            webSetting = this.settings
        }

        webSetting.apply {
            this.javaScriptEnabled = true
            userAgentString = USER_AGENT_WEB
            setSupportMultipleWindows(false)
            javaScriptCanOpenWindowsAutomatically = false
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(false)
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            cacheMode = WebSettings.LOAD_NO_CACHE
        }
    }

    private fun addCart(item: List<CartItem>?) {
        if (item.isNullOrEmpty())
            return

        val cart = Cart(
            cartTypeCd = "10",
            infloSiteNo = "",
            items = item
        )

        val url = FormatUtil.jsItemCartUrl(cart)

        binding.webView.loadUrl(url)
        binding.webView.reload()
    }

    override fun setListener() {
        binding.btnBack.setOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                finish()
            }
        }
    }

    private fun showLoading(showing: Boolean) {

        val visible = View.VISIBLE
        val gone = View.GONE

        // showing == true -> loading 보여줌
        if (showing) {
            binding.container1.visibility = visible
            binding.container2.visibility = gone
        } else {
            binding.container1.visibility = gone
            binding.container2.visibility = visible
        }

    }

    private fun webViewPopUpInterrupt() = object : WebChromeClient() {

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {

            return super.onConsoleMessage(consoleMessage)
        }

        override fun onJsConfirm(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {

            AlertDialog.Builder(this@PaymentActivity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    viewModel.updateState(State.ADD_READY)
                    result!!.confirm()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    viewModel.updateState(State.ADD_READY)
                    result!!.cancel()
                }
                .setCancelable(false)
                .create()
                .show()
            return true
        }
    }

    private fun pageFinishListener() = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            val state = viewModel.currentState

            // 상태 업데이트만.
            when (state.value) {
                State.INIT -> {
                    viewModel.updateState(State.CHECK_CART)
                }                State.CHECK_CART -> {
                    when (url) {
                        goToAddCart -> viewModel.updateState(State.ADD_CART)
                        goToClearCart -> {
                            viewModel.updateState(State.CLEAR_CART)
                        }
                    }
                }
                State.CLEAR_CART -> {
                    // 상태 업데이트 안함.
                }
                State.ADD_READY -> {
                    viewModel.updateState(State.ADD_CART)
                }
                State.ADD_CART -> {
                    viewModel.updateState(State.FINISH)
                }
                State.FINISH -> {
                    showLoading(false)
                }
            }
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed()
        }
    }

    @SuppressLint("JavascriptInterface")
    override fun setObserver() {
        viewModel.currentState.observe(this) { state ->

            // 실질적 동작
            when (state!!) {
                State.INIT -> {
                    binding.webView.loadUrl(goToCheckCart)
                }
                State.CHECK_CART -> {
                    binding.webView.evaluateJavascript(
                        "(function() {" +
                                "var empty_size = \$(\"#emptyItem\").children().size();\n" +
                                "var isEmpty = (empty_size != 0);\n" +
                                "return { \"data\" : isEmpty}" +
                                "})()",
                        ValueCallback<String?> { response ->
                            try {
                                val jo = JSONObject(response)
                                val result = jo.getBoolean("data")

                                if (result) {
                                    binding.webView.loadUrl(goToAddCart)
                                } else {
                                    binding.webView.loadUrl(goToClearCart)
                                }
                            } catch (e: Exception) {
                                binding.webView.loadUrl(goToAddCart)
                                e.printStackTrace()
                            }
                        }
                    )
                }
                State.CLEAR_CART -> {
                    webSetting.userAgentString = USER_AGENT_APP
                    binding.webView.loadUrl("javascript:${viewModel.clearJs}")
                }

                State.ADD_READY -> {
                    binding.webView.loadUrl(goToAddCart)
                }

                State.ADD_CART -> {
                    webSetting.userAgentString = USER_AGENT_WEB
                    addCart(viewModel.wishList)
                }

                State.FINISH -> {
                    webSetting.userAgentString = USER_AGENT_APP
                    binding.webView.apply {
                        webSetting = this.settings
                    }
                    binding.webView.loadUrl(goToFinish)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

//        CookieSyncManager.getInstance().stopSync();
    }

}


private val USER_AGENT_WEB =
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36"
private val USER_AGENT_APP =
    "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19"