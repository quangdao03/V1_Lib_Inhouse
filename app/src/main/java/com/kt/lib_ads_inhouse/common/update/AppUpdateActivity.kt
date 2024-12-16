/**
 * goi truoc khi vao man hinh main de update
 */

package com.kt.lib_ads_inhouse.common.update

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.kt.stylelist.outfit.changehair.makeup.ui.my.view.tap
import com.lib.admob.resumeAds.AppOpenResumeManager
import com.util.FirebaseLogEventUtils
import com.util.RemoteConfig
import com.util.appupdate.AppUpdateManager
import com.util.appupdate.RemoteConfigVersionUpdateModel
import com.v1.chat.anything.R
import com.v1.chat.anything.a1_common_utils.RemoteConfigKey
import com.kt.lib_ads_inhouse.a1_common_utils.ad.AdCommon
import com.v1.chat.anything.a1_common_utils.base.BaseActivity
import com.v1.chat.anything.a1_common_utils.base.BaseViewModel
import com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.screens.RemoteConfigScreenAppUpdateModel
import com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.screens.RemoteConfigScreenLanguageStartModel
import com.v1.chat.anything.a8_app_utils.Constants
import com.v1.chat.anything.a8_app_utils.SystemUtil
import com.v1.chat.anything.databinding.ActivityAppUpdateBinding
import com.kt.lib_ads_inhouse.common.intro.IntroActivity
import com.kt.lib_ads_inhouse.common.splash.SplashWithProgessActivity
import com.v1.chat.anything.ui.home.home_options.HomeActivityOp1
import com.v1.chat.anything.ui.main.MainActivity

class AppUpdateActivity : BaseActivity<ActivityAppUpdateBinding, BaseViewModel>() {

    private var remoteConfigScreenAppUpdateModel : RemoteConfigScreenAppUpdateModel?=null

    override fun createBinding() = ActivityAppUpdateBinding.inflate(layoutInflater)

    override fun setViewModel() = BaseViewModel()

    override fun initView() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
        binding.tvContent.setText(binding.tvContent.text.toString() + AppUpdateManager.updateContent)

        //btn update click
        binding.btnSubmit.tap {
            SystemUtil.openAppInPlayStore(this)
            val bundle = Bundle()

            AppOpenResumeManager.setEnableAdsResume(false)
            FirebaseLogEventUtils.logEventCommonWithName(this, "update_ok", bundle)
        }

        //btn cancel
        if (AppUpdateManager.checkUpdate  == 2){
            binding.btnCancel.visibility = View.GONE

        }

        //check next screen
        remoteConfigScreenAppUpdateModel = RemoteConfig.getConfigObject(
            this,
            RemoteConfigKey.screen_app_update,
            RemoteConfigScreenAppUpdateModel::class.java
        )

        var nextScreen = SystemUtil.getActivityClass(
            remoteConfigScreenAppUpdateModel?.next_screen_default,
            HomeActivityOp1::class.java
        )

        binding.btnCancel.tap {
            startActivity(Intent(this, nextScreen))
            val bundle = Bundle()
            FirebaseLogEventUtils.logEventCommonWithName(this, "update_cancel", bundle)
        }
        binding.ivWhatNew.tap {
            if (AppUpdateManager.updateContent.isNotEmpty()) {
                if (binding.tvContent.visibility == View.VISIBLE) {
                    binding.tvContent.visibility = View.GONE
                    binding.ivWhatNew.setBackgroundResource(R.drawable.ic_whatsnew)
                } else {
                    binding.tvContent.visibility = View.VISIBLE
                    binding.ivWhatNew.setBackgroundResource(R.drawable.ic_whatnew_dismiss)
                    if (!binding.tvContent.text.toString().contains(AppUpdateManager.updateContent)) {
                        binding.tvContent.text =
                            binding.tvContent.text.toString() + AppUpdateManager.updateContent
                    }
                }
            }
        }

    }

    override fun initAd() {
        //native
        AdCommon.loadAndShowAdNativeAdvance(lifecycle,
            RemoteConfigKey.ad_native_app_update,
            binding.frAd,
            null)
    }

}