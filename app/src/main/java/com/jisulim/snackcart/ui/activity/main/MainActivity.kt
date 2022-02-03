package com.jisulim.snackcart.ui.activity.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import com.jisulim.snackcart.R
import com.jisulim.snackcart.databinding.ActivityMainBinding
import com.jisulim.snackcart.ui.activity.BaseActivity
import com.jisulim.snackcart.ui.fragment.BaseFragment
import com.jisulim.snackcart.ui.fragment.CartFragment
import com.jisulim.snackcart.ui.fragment.SearchFragment

// @AndroidEntryPoint -> annotaion을 붙여서 컴포넌트를 만들고, Dependency를 주입받을 수 있도록 한다.
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this@MainActivity

    }

    override fun onResume() {
        super.onResume()

        initNavigationBar()
    }

    override fun setObserver() {

        viewModel.fragmentStatus.observe(this) {
            // fragment 변경.
            changeFragment( goTo(it) )
        }
    }

    private fun initNavigationBar() =
        binding.navigationBar.run {
            setOnApplyWindowInsetsListener(null)
            setOnItemSelectedListener {
                // 상태 업데이트만.
                viewModel.updateFragmentStatus(it.itemId)

                true
            }
        }

    private fun goTo(id: Int): BaseFragment = when (id) {
        R.id.search -> SearchFragment()
        R.id.cart -> CartFragment()
        else -> SearchFragment()
    }

    private fun changeFragment(fragment: Fragment) =
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fg_container, fragment)
            .commit()

}