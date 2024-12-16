package com.kt.lib_ads_inhouse.common.welcomeback

import com.util.RemoteConfig

import com.kt.lib_ads_inhouse.a1_common_utils.ad.AdCommon
import com.kt.lib_ads_inhouse.a1_common_utils.base.BaseActivity

class WelcomeBackActivity : BaseActivity<ActivityWelcomeBackBinding, BaseViewModel>() {

    var remoteConfigAppAllModel: RemoteConfigAppAllModel? = null

    override fun createBinding() = ActivityWelcomeBackBinding.inflate(layoutInflater)
    override fun setViewModel() = BaseViewModel()

    override fun initView() {
        super.initView()

        remoteConfigAppAllModel = RemoteConfig.getConfigObject(
            this,
            RemoteConfigKey.app_config,
            RemoteConfigAppAllModel::class.java
        )

        binding.title.setText(remoteConfigAppAllModel?.welcomeback_tittle)

        binding.tvContinue.setOnClickListener {
            finish()
        }
    }

    override fun initAd() {
        //native
        AdCommon.loadAndShowAdNativeAdvance(lifecycle,
            RemoteConfigKey.ad_native_welcomeback,
            binding.frAd,
            null)
    }

}