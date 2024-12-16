/**
 *hien tai neu khong co intenet den man hinh nointernet\
 * sau do co internet thi se bat lai app tu man splash cho du truoc do o man hinh khac
 */


package com.kt.lib_ads_inhouse.common.no_internet

import android.content.Intent
import android.provider.Settings
import com.util.CheckInternet
import com.v1.chat.anything.a1_common_utils.base.BaseActivity
import com.v1.chat.anything.a1_common_utils.base.BaseViewModel
import com.v1.chat.anything.databinding.ActivityNoInternetBinding
import com.kt.lib_ads_inhouse.common.language.LanguageStartActivity
import com.kt.lib_ads_inhouse.common.splash.SplashWithProgessActivity

class NoInternetActivity : BaseActivity<ActivityNoInternetBinding, BaseViewModel>() {
    override fun createBinding(): ActivityNoInternetBinding {
        return ActivityNoInternetBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()

        binding.tvRetry.setOnClickListener {
            if (CheckInternet.haveNetworkConnection(this)) {
                //Truong edit 20241008
                //fix khi co internet thi luon quay lai man splash
//                if (SplashWithProgessActivity.isLanguageStart) {
//                    startActivity(Intent(this, LanguageStartActivity::class.java))
//                    finish()
//                } else {
//                    finish()
//                }
                startActivity(Intent(this, SplashWithProgessActivity::class.java))
                finish()

            } else {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (CheckInternet.haveNetworkConnection(this)) {
            //Truong edit 20241008
            //fix khi co internet thi luon quay lai man splash
//            if (SplashWithProgessActivity.isLanguageStart) {
//                startActivity(Intent(this, LanguageStartActivity::class.java))
//                finish()
//            } else {
//                finish()
//            }
            startActivity(Intent(this, SplashWithProgessActivity::class.java))
            finish()
        }
    }
}