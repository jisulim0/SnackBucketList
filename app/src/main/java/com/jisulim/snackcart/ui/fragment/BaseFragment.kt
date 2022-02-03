package com.jisulim.snackcart.ui.fragment

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.support.v4.runOnUiThread
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment : Fragment() {
    val viewModel by viewModels<FragmentViewModel>()

    @Inject
    lateinit var glide: RequestManager

    open fun setViewHolderManager() {}

    fun dialog(
        title: String? = null,
        message: String? = null,
        positive: (() -> Any)? = null,
        negative: (() -> Any)? = null
    ) {
        runOnUiThread {
            MaterialDialog(this.requireContext()).show {
                if (title != null) {
                    title(text = title)
                }
                if (message != null) {
                    message(text = message)
                }
                if (positive != null) {
                    positiveButton(text = "예") {
                        positive()
                    }
                }
                if (negative != null) {
                    negativeButton(text = "아니오"){
                        negative()
                    }
                }
            }
        }
    }

    open fun setObserver() {}

    open fun setListener() {}

    fun keyDown() {
        val imm =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}