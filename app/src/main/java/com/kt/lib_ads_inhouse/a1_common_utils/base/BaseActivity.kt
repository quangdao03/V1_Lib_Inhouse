package com.kt.lib_ads_inhouse.a1_common_utils.base

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.kt.lib_ads_inhouse.a8_app_utils.SystemUtil
import com.util.FirebaseLogEventUtils


abstract class BaseActivity<VB : ViewBinding, V : ViewModel> : AppCompatActivity() {
    protected lateinit var binding: VB
    protected lateinit var viewModel: V

    abstract fun createBinding(): VB
    abstract fun setViewModel(): V

    protected open fun initView() {}
    protected open fun bindView() {}

    open fun viewModel() {}
    open fun initData() {}

    //init ad
    protected open fun initAd() {}

    //adjust layout
    protected open fun adjustLayout() {}

    override fun hasWindowFocus(): Boolean {
        hideNavigationBar()
        return super.hasWindowFocus()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideNavigationBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemUtil.setLocale(this)
        binding = createBinding()
        setContentView(binding.root)
        viewModel = setViewModel()
        viewModel()
        initView()
        bindView()

        //AD implement
        initAd()

        //hide or show navigation bar
        hideNavigationBar()

        //adjust layout
        adjustLayout()

        //log event
        FirebaseLogEventUtils.logEventScreenView(this, "onCreat")
    }


    //confirm xu ly source code outdated
    protected open fun hideNavigationBar() {

        val decorView = window.decorView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 (API level 30) and above
            decorView.windowInsetsController?.let { controller ->
                controller.hide(WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Below Android 11
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )

            // Listener để ẩn lại thanh điều hướng khi người dùng tương tác
            decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    Handler().postDelayed({
                        decorView.systemUiVisibility = (
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                )
                    }, 3000)
                }
            }
        }
    }


}