package com.jisulim.snackcart.ui.activity.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jisulim.snackcart.models.dto.CartItem
import com.jisulim.snackcart.ui.State

class PaymentViewModel : ViewModel() {

    lateinit var wishList: List<CartItem>

    val clearJs: String =
        "var checked = \$(\"[name=btChekAll]\").is(\":checked\");\n" +
                "if(!checked){\n" +
                "  \$(\"[name=btChekAll]\").click();\n" +
                "}\n" +
                "\$(\"[name=btDelChekItemAll]\").first().click();"

    val getCartItemCountJs =
        "\$(\"[name=btChekAll]\").is(\":checked\");"

    val loginJs: String = "id = document.getElementById('inp_id');\n" +
            "id.value='chltmdgus06@naver.com';\n" +
            "pw = document.getElementById('inp_pw');\n" +
            "pw.value='alswns09!';\n" +
            "\n" +
            "loginModel.login()"


    private var _currentState = MutableLiveData<State>()

    val currentState: LiveData<State>
        get() = _currentState

    fun updateState(state: State) {
        _currentState.value = state
    }

}